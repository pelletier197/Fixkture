package io.github.pelletier197.fixkture.domain

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiUtil

class InstantiationStatementGenerator(
        private val element: PsiElement,
        private val fieldBuilder: InstantiationFieldBuilder,
        private val context: PsiElementInstantiationStatementBuilderContext) {
    fun createKotlinStatement(): String {
        val context = getFieldConstructionContext()
        val targetClass = element as PsiClass
        return "val ${context.fieldName}: ${targetClass.qualifiedName} = ${fieldBuilder.asKotlinFlatValue(context)}"
    }

    fun createJavaStatement(): String {
        val context = getFieldConstructionContext()
        val targetClass = element as PsiClass
        return "public static final ${targetClass.qualifiedName} ${context.fieldName} = ${fieldBuilder.asJavaFlatValue(context)};"
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


fun generateInstantiationStatement(element: PsiElement, context: PsiElementInstantiationStatementBuilderContext): InstantiationStatementGenerator {
    val fieldBuilder = createInstantiationField(context)
    return InstantiationStatementGenerator(
            fieldBuilder = fieldBuilder,
            context = context,
            element = element
    )
}