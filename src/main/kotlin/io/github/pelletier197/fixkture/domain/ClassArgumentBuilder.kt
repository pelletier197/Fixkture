package io.github.pelletier197.fixkture.domain

import com.intellij.psi.PsiClass

class InstantiationStatementGenerator(
    private val element: PsiClass,
    private val fieldBuilder: InstantiationFieldBuilder,
    private val context: PsiElementInstantiationStatementBuilderContext
) {
    fun createKotlinStatement(): String {
        val context = getFieldConstructionContext()
        return "val ${context.fieldName} = ${fieldBuilder.asKotlinFlatValue(context)}"
    }

    fun createJavaStatement(): String {
        val context = getFieldConstructionContext()
        val targetClass = element
        return "public static final ${targetClass.qualifiedName} ${context.fieldName} = ${fieldBuilder.asJavaFlatValue(context)};"
    }

    private fun generateFieldName(): String {
        return context.variableNameGenerator(element)
    }

    private fun getFieldConstructionContext(): FieldConstructionContext {
        return FieldConstructionContext(
            fieldName = generateFieldName(),
            targetElement = TargetElement.of(element),
            constructorSelector = context.constructorSelector,
            interfaceImplementationSelector = context.interfaceImplementationSelector,
            variableNameGenerator = context.variableNameGenerator,
        )
    }
}

fun generateInstantiationStatement(element: PsiClass, context: PsiElementInstantiationStatementBuilderContext): InstantiationStatementGenerator {
    val fieldBuilder = createInstantiationField(context)
    return InstantiationStatementGenerator(
        fieldBuilder = fieldBuilder,
        context = context,
        element = element
    )
}
