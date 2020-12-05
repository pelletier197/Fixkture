package io.github.pelletier197.fixkture.domain.generator.java

import com.intellij.psi.*
import com.intellij.psi.util.PsiUtil
import io.github.pelletier197.fixkture.domain.*
import io.github.pelletier197.fixkture.domain.generator.CallbackClassInstantiationFieldBuilder
import io.github.pelletier197.fixkture.domain.generator.LanguageCallbackValueGenerator
import io.github.pelletier197.fixkture.domain.generator.NullInstantiationField

private data class CollectionElementInstantiationFieldBuilder(
        private val elementBuilder: InstantiationFieldBuilder,
        private val targetElement: TargetElement,
) : InstantiationFieldBuilder {
    override fun asJavaConstructorArgument(context: FieldConstructionContext): String {
        return elementBuilder.asJavaConstructorArgument(context.copy(targetElement = targetElement))
    }

    override fun asKotlinConstructorArgument(context: FieldConstructionContext): String {
        return elementBuilder.asKotlinConstructorArgument(context.copy(targetElement = targetElement))
    }

    override fun asJavaFlatValue(context: FieldConstructionContext): String {
        return elementBuilder.asJavaFlatValue(context.copy(targetElement = targetElement))
    }

    override fun asKotlinFlatValue(context: FieldConstructionContext): String {
        return elementBuilder.asKotlinFlatValue(context.copy(targetElement = targetElement))
    }

}

object JavaCollectionGenerator {
    fun generateList(): InstantiationFieldBuilder {
        return CallbackClassInstantiationFieldBuilder(
                LanguageCallbackValueGenerator(
                        java = { context -> "java.util.List.of(${createCollectionArgument(context = context).asJavaFlatValue(context)})" },
                        kotlin = { context -> "listOf(${createCollectionArgument(context = context).asKotlinFlatValue(context)})" }
                )
        )
    }

    fun generateIterable(): InstantiationFieldBuilder {
        return generateList()
    }

    private fun createCollectionArgument(context: FieldConstructionContext): InstantiationFieldBuilder {
        val element = context.targetElement.element
        if (element is PsiParameter) {
            val targetClass = extractListElementClass(element) ?: return NullInstantiationField()

            return CollectionElementInstantiationFieldBuilder(
                    elementBuilder = createInstantiationField(
                            context = context.asClassInstantiationStatementBuilderContext(TargetElement.of(targetClass))
                    ),
                    targetElement = TargetElement.of(targetClass)
            )

        }
        return NullInstantiationField()
    }

    private fun extractListElementClass(element: PsiParameter): PsiClass? {
        val parameterType = extractListElementType(element)
        return PsiUtil.resolveClassInType(parameterType)
    }

    private fun extractListElementType(element: PsiParameter): PsiType? {
        val parameterType = PsiUtil.extractIterableTypeParameter(element.type, false)

        if (parameterType is PsiWildcardType) {
            return parameterType.bound
        }

        return parameterType
    }
}