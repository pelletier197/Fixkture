package io.github.pelletier197.fixkture.domain

import com.intellij.psi.PsiClass
import com.intellij.psi.util.PsiUtil

class InstantiationStatementGenerator(
        private val element: PsiClass,
        private val fieldBuilder: InstantiationFieldBuilder,
        private val context: PsiElementInstantiationStatementBuilderContext) {
    fun createKotlinStatement(): String {
        val context = getFieldConstructionContext()
        return "val ${context.fieldName}: ${element.qualifiedName} = ${fieldBuilder.asKotlinFlatValue(context)}"
    }

    fun createJavaStatement(): String {
        val context = getFieldConstructionContext()
        return "public static final ${element.qualifiedName} ${context.fieldName} = ${fieldBuilder.asJavaFlatValue(context)};"
    }

    private fun generateFieldName(): String {
        return PsiUtil.getName(element)?.decapitalize() ?: "fixture"
    }

    private fun getFieldConstructionContext(): FieldConstructionContext {
        return FieldConstructionContext(
                fieldName = generateFieldName(),
                targetElement = TargetElement.of(element),
                constructorSelector = context.constructorSelector,
        )
    }
}


fun generateInstantiationStatement(psiClass: PsiClass, context: PsiElementInstantiationStatementBuilderContext): InstantiationStatementGenerator {
    val fieldBuilder = createInstantiationField(context)
    return InstantiationStatementGenerator(
            fieldBuilder = fieldBuilder,
            context = context,
            element = psiClass
    )
}