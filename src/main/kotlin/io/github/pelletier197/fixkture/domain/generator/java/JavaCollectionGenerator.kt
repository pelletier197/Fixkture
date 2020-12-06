package io.github.pelletier197.fixkture.domain.generator.java

import com.intellij.psi.*
import com.intellij.psi.impl.source.PsiClassReferenceType
import com.intellij.psi.util.PsiUtil
import io.github.pelletier197.fixkture.domain.*
import io.github.pelletier197.fixkture.domain.generator.LanguageCallbackInstantiationFieldBuilder
import io.github.pelletier197.fixkture.domain.generator.java.Utils.extractType

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
        return LanguageCallbackInstantiationFieldBuilder(
                java = { context -> "java.util.List.of(${createCollectionArgument(context = context).asJavaFlatValue(context)})" },
                kotlin = { context -> "listOf(${createCollectionArgument(context = context).asKotlinFlatValue(context)})" }
        )
    }

    fun generateSet(): InstantiationFieldBuilder {
        return LanguageCallbackInstantiationFieldBuilder(
                java = { context -> "java.util.Set.of(${createCollectionArgument(context = context).asJavaFlatValue(context)})" },
                kotlin = { context -> "setOf(${createCollectionArgument(context = context).asKotlinFlatValue(context)})" }
        )
    }

    fun generateHashset(): InstantiationFieldBuilder {
        val setBuilder = generateSet()
        return LanguageCallbackInstantiationFieldBuilder(
                java = { context -> "new java.util.HashSet<>(${setBuilder.asJavaFlatValue(context)})" },
                kotlin = { context -> "HashSet(${setBuilder.asKotlinFlatValue(context)})" }
        )
    }

    fun generateTreeSet(): InstantiationFieldBuilder {
        val setBuilder = generateSet()
        return LanguageCallbackInstantiationFieldBuilder(
                java = { context -> "new java.util.TreeSet<>(${setBuilder.asJavaFlatValue(context)})" },
                kotlin = { context -> "TreeSet(${setBuilder.asKotlinFlatValue(context)})" }
        )
    }

    fun generateArrayList(): InstantiationFieldBuilder {
        val listBuilder = generateList()
        return LanguageCallbackInstantiationFieldBuilder(
                java = { context -> "new java.util.ArrayList<>(${listBuilder.asJavaFlatValue(context)})" },
                kotlin = { context -> "ArrayList(${listBuilder.asKotlinFlatValue(context)})" }
        )
    }

    fun generateLinkedList(): InstantiationFieldBuilder {
        val listBuilder = generateList()
        return LanguageCallbackInstantiationFieldBuilder(
                java = { context -> "new java.util.LinkedList<>(${listBuilder.asJavaFlatValue(context)})" },
                kotlin = { context -> "LinkedList(${listBuilder.asKotlinFlatValue(context)})" }
        )
    }

    fun generateIterable(): InstantiationFieldBuilder {
        return generateList()
    }

    fun generateMap(): InstantiationFieldBuilder {
        return LanguageCallbackInstantiationFieldBuilder(
                java = { context -> "java.util.Map.of(${createMapKeyBuilder(context).asJavaFlatValue(context)}, ${createMapValueBuilder(context).asJavaFlatValue(context)})" },
                kotlin = { context -> "mapOf(${createMapKeyBuilder(context).asKotlinFlatValue(context)} to ${createMapValueBuilder(context).asKotlinFlatValue(context)})" }
        )
    }

    private fun createMapKeyBuilder(context: FieldConstructionContext): InstantiationFieldBuilder {
        return createMapArgument(context, 0)
    }

    private fun createMapValueBuilder(context: FieldConstructionContext): InstantiationFieldBuilder {
        return createMapArgument(context, 1)
    }

    private fun createMapArgument(context: FieldConstructionContext, parameterIndex: Int): InstantiationFieldBuilder {
        val type = extractType(context) ?: return NullInstantiationField()

        if (type is PsiClassReferenceType) {
            val parameterTypes = type.reference.typeParameters
            return createArgument(context, parameterTypes[parameterIndex])
        }

        return NullInstantiationField()
    }

    private fun createCollectionArgument(context: FieldConstructionContext): InstantiationFieldBuilder {
        val targetType = getIterableElementType(context) ?: return NullInstantiationField()
        return createArgument(context, targetType)
    }

    private fun createArgument(context: FieldConstructionContext, targetType: PsiType): InstantiationFieldBuilder {
        return CollectionElementInstantiationFieldBuilder(
                elementBuilder = createInstantiationField(
                        context = context.asClassInstantiationStatementBuilderContext(TargetElement.of(targetType))
                ),
                targetElement = TargetElement.of(targetType)
        )
    }

    private fun getIterableElementType(context: FieldConstructionContext): PsiType? {
        return extractType(context)?.let { extractListElementTypeFromType(it) }
    }

    private fun extractListElementTypeFromType(element: PsiType): PsiType? {
        val parameterType = PsiUtil.extractIterableTypeParameter(element, false)

        if (parameterType is PsiWildcardType) {
            return parameterType.bound
        }

        return parameterType
    }
}