package io.github.pelletier197.fixkture.domain

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiUtil

class InstantiationStatementGenerator(private val fieldBuilder: InstantiationFieldBuilder,
                                      private val context: PsiElementInstantiationStatementBuilderContext) {
    fun createKotlinStatement(): String {
        val context = getFieldConstructionContext()
        val element = targetElement as PsiClass
        return "val ${context.fieldName}: ${element.qualifiedName} = ${fieldBuilder.asKotlinFlatValue(context)}"
    }

    fun createJavaStatement(): String {
        val context = getFieldConstructionContext()
        val element = targetElement as PsiClass
        return "public static final ${element.qualifiedName} ${context.fieldName} = ${fieldBuilder.asJavaFlatValue(context)};"
    }

    private fun generateFieldName(): String {
        return PsiUtil.getName(targetElement)?.decapitalize() ?: "fixture"
    }

    private val targetElement: PsiElement = context.targetElement

    private fun getFieldConstructionContext(): FieldConstructionContext {
        return FieldConstructionContext(
                fieldName = generateFieldName(),
                targetElement = targetElement,
                constructorSelector = context.constructorSelector,
        )
    }
}


fun generateInstantiationStatement(context: PsiElementInstantiationStatementBuilderContext): InstantiationStatementGenerator {
    val fieldBuilder = createInstantiationField(context)
    return InstantiationStatementGenerator(
            fieldBuilder = fieldBuilder,
            context = context
    )
}