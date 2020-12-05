package io.github.pelletier197.fixkture.domain.generator.java

import com.intellij.psi.*
import com.intellij.psi.util.PsiUtil
import io.github.pelletier197.fixkture.domain.*
import io.github.pelletier197.fixkture.domain.generator.CallbackClassInstantiationFieldBuilder
import io.github.pelletier197.fixkture.domain.generator.LanguageCallbackValueGenerator

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

    fun generateSet(): InstantiationFieldBuilder {
        return CallbackClassInstantiationFieldBuilder(
                LanguageCallbackValueGenerator(
                        java = { context -> "java.util.Set.of(${createCollectionArgument(context = context).asJavaFlatValue(context)})" },
                        kotlin = { context -> "setOf(${createCollectionArgument(context = context).asKotlinFlatValue(context)})" }
                )
        )
    }

    fun generateArrayList(): InstantiationFieldBuilder {
        val listBuilder = generateList()
        return CallbackClassInstantiationFieldBuilder(
                LanguageCallbackValueGenerator(
                        java = { context -> "new ArrayList<>()" },
                        kotlin = { context -> "listOf(${createCollectionArgument(context = context).asKotlinFlatValue(context)})" }
                )
        )
    }

    fun generateIterable(): InstantiationFieldBuilder {
        return generateList()
    }

    private fun createCollectionArgument(context: FieldConstructionContext): InstantiationFieldBuilder {
        val targetType = getListElementType(context) ?: return NullInstantiationField()

        return CollectionElementInstantiationFieldBuilder(
                elementBuilder = createInstantiationField(
                        context = context.asClassInstantiationStatementBuilderContext(TargetElement.of(targetType))
                ),
                targetElement = TargetElement.of(targetType)
        )
    }

    private fun getDiamondTypeString(context: FieldConstructionContext) : String {
        val elementType = getListElementType(context) ?: return "?"

    }
    private fun getListElementType(context: FieldConstructionContext): PsiType? {
        return when (val element = context.targetElement.element) {
            is PsiParameter -> extractListElementTypeFromType(element.type)
            is PsiType -> extractListElementTypeFromType(element)
            else -> null
        }
    }

//    private fun extractListElementClass(element: PsiParameter): PsiClass? {
//        val parameterType = extractListElementType(element)
//        return PsiUtil.resolveClassInType(parameterType)
//    }

    private fun extractListElementTypeFromType(element: PsiType): PsiType? {
        val parameterType = PsiUtil.extractIterableTypeParameter(element, false)

        if (parameterType is PsiWildcardType) {
            return parameterType.bound
        }

        return parameterType
    }
}