package io.github.pelletier197.fixkture.domain

import com.intellij.psi.PsiClass

class InstantiationStatementGenerator(private val targetClass: PsiClass,
                                      private val fieldBuilder: InstantiationFieldBuilder) {
    fun createKotlinStatement(): String {
        val context = getFieldConstructionContext()
        return "val ${context.fieldName}: ${targetClass.qualifiedName} = ${fieldBuilder.asKotlinFlatValue(context)}"
    }

    fun createJavaStatement(): String {
        val context = getFieldConstructionContext()
        return "public static final ${targetClass.qualifiedName} ${context.fieldName} = ${fieldBuilder.asJavaFlatValue(context)};"
    }

    private fun generateFieldName(): String {
        return targetClass.name?.decapitalize() ?: "fixture"
    }

    private fun getFieldConstructionContext(): FieldConstructionContext {
        return FieldConstructionContext(
                fieldName = generateFieldName()
        )
    }
}


fun generateInstantiationStatement(context: ClassInstantiationStatementBuilderContext): InstantiationStatementGenerator {
    val fieldBuilder = createInstantiationField(context)
    return InstantiationStatementGenerator(
            targetClass = context.targetClass,
            fieldBuilder = fieldBuilder
    )
}