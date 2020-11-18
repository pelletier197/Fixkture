package io.github.pelletier197.fixkture.domain

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiParameter
import com.intellij.psi.util.PsiUtil
import io.github.pelletier197.fixkture.domain.generator.ClassGenerator
import io.github.pelletier197.fixkture.domain.generator.JavaTimeGenerator
import io.github.pelletier197.fixkture.domain.generator.PrimitiveGenerator
import java.lang.UnsupportedOperationException

data class FieldConstructionContext(
        val fieldName: String
)

interface InstantiationFieldBuilder {
    fun asJavaConstructorArgument(context: FieldConstructionContext): String
    fun asKotlinConstructorArgument(context: FieldConstructionContext): String
    fun asJavaFlatValue(context: FieldConstructionContext): String
    fun asKotlinFlatValue(context: FieldConstructionContext): String
}

fun createInstantiationField(context: PsiElementInstantiationStatementBuilderContext): InstantiationFieldBuilder {
    return createInstantiationFieldIfPossible(context = context)
            ?: throw UnsupportedOperationException("Generating a fixture for element of type ${context.targetElement} is not supported")
}

fun createInstantiationFieldIfPossible(context: PsiElementInstantiationStatementBuilderContext): InstantiationFieldBuilder? {
    return when (val element = context.targetElement) {
        is PsiClass -> when (element.qualifiedName) {
            "Boolean", "java.lang.Boolean" -> PrimitiveGenerator.generateBoolean()
            "Integer", "java.lang.Integer" -> PrimitiveGenerator.generateInteger()
            "Long", "java.lang.Long" -> PrimitiveGenerator.generateLong()
            "Float", "java.lang.Float" -> PrimitiveGenerator.generateFloat()
            "Double", "java.lang.Double" -> PrimitiveGenerator.generateDouble()
            "Byte", "java.lang.Byte" -> PrimitiveGenerator.generateByte()
            "Char", "java.lang.Character" -> PrimitiveGenerator.generateChar()
            "String", "java.lang.String" -> PrimitiveGenerator.generateString(context)
            "UUID", "java.util.UUID" -> PrimitiveGenerator.generateUUID()
            "Instant", "java.time.Instant" -> JavaTimeGenerator.generateInstant()
            "ZonedDateTime", "java.time.ZonedDateTime" -> JavaTimeGenerator.generateZoneDateTime()
            "ZoneId", "java.time.ZoneId" -> JavaTimeGenerator.generateZoneId()
            "LocalDate", "java.time.LocalDate" -> JavaTimeGenerator.generateLocalDate()
            "LocalDateTime", "java.time.LocalDateTime" -> JavaTimeGenerator.generateLocalDateTime()
            "LocalTime", "java.time.LocalTime" -> JavaTimeGenerator.generateLocalTime()
            "Period", "java.time.Period" -> JavaTimeGenerator.generatePeriod()
            else -> ClassGenerator.generateClass(context.asClassInstantiationContext())
        }
        is PsiParameter -> handlePsiParameter(element, context)
        else -> null
    }
}

private fun handlePsiParameter(parameter: PsiParameter, context: PsiElementInstantiationStatementBuilderContext): InstantiationFieldBuilder? {
    val classType = PsiUtil.resolveClassInType(parameter.type)
    if (classType != null) return createInstantiationFieldIfPossible(context.copy(targetElement = classType))
    return null
}