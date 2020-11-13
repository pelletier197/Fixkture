package io.github.pelletier197.fixkture.domain

import io.github.pelletier197.fixkture.domain.generator.PrimitiveGenerator

data class FieldConstructionContext(
        val fieldName: String
)

interface InstantiationFieldBuilder {
    fun asJavaConstructorArgument(context: FieldConstructionContext): String
    fun asKotlinConstructorArgument(context: FieldConstructionContext): String
    fun asJavaFlatValue(context: FieldConstructionContext): String
    fun asKotlinFlatValue(context: FieldConstructionContext): String
}

fun createInstantiationField(context: ClassInstantiationStatementBuilderContext): InstantiationFieldBuilder {
    val psiClass = context.targetClass

    when (psiClass.qualifiedName) {
        "Boolean", "java.lang.Boolean" -> PrimitiveGenerator.generateBoolean()
        "Integer", "java.lang.Integer" -> PrimitiveGenerator.generateInteger()
        "Long", "java.lang.Long" -> PrimitiveGenerator.generateLong()
        "Float", "java.lang.Float" -> PrimitiveGenerator.generateFloat()
        "Double", "java.lang.Double" -> PrimitiveGenerator.generateDouble()
        "Byte", "java.lang.Byte" -> PrimitiveGenerator.generateByte()
        "Char", "java.lang.Character" -> PrimitiveGenerator.generateChar()
        "String", "java.lang.String" -> PrimitiveGenerator.generateString(context)
    }

    TODO()
}