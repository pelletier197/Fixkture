package io.github.pelletier197.fixkture.domain

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiParameter
import com.intellij.psi.PsiPrimitiveType
import com.intellij.psi.util.PsiUtil
import io.github.pelletier197.fixkture.domain.generator.ClassGenerator
import io.github.pelletier197.fixkture.domain.generator.NullInstantiationField
import io.github.pelletier197.fixkture.domain.generator.java.JavaLibraryGenerator
import io.github.pelletier197.fixkture.domain.generator.java.JavaTimeGenerator
import io.github.pelletier197.fixkture.domain.generator.PrimitiveGenerator
import io.github.pelletier197.fixkture.domain.generator.java.JavaCollectionGenerator

data class FieldConstructionContext(
        val fieldName: String,
        val targetElement: TargetElement,
        val constructorSelector: ConstructorSelectionFunction
) {
    fun asClassInstantiationStatementBuilderContext(element: TargetElement): PsiElementInstantiationStatementBuilderContext {
        return PsiElementInstantiationStatementBuilderContext(
                targetElement = element,
                constructorSelector = this.constructorSelector
        )
    }
}

interface InstantiationFieldBuilder {
    fun asJavaConstructorArgument(context: FieldConstructionContext): String
    fun asKotlinConstructorArgument(context: FieldConstructionContext): String
    fun asJavaFlatValue(context: FieldConstructionContext): String
    fun asKotlinFlatValue(context: FieldConstructionContext): String
}

fun createInstantiationField(context: PsiElementInstantiationStatementBuilderContext): InstantiationFieldBuilder {
    return createInstantiationFieldIfPossible(context = context) ?: NullInstantiationField()
}

fun createInstantiationFieldIfPossible(context: PsiElementInstantiationStatementBuilderContext): InstantiationFieldBuilder? {
    return when (val element = context.targetElement.element) {
        is PsiClass -> handlePsiClass(element, context)
        is PsiParameter -> handlePsiParameter(element, context)
        else -> null
    }
}

private fun handlePsiClass(element: PsiClass, context: PsiElementInstantiationStatementBuilderContext): InstantiationFieldBuilder? {
    return when (element.qualifiedName) {
        // Primitives
        "Boolean", "java.lang.Boolean" -> PrimitiveGenerator.generateBoolean()
        "Integer", "java.lang.Integer" -> PrimitiveGenerator.generateInteger()
        "Long", "java.lang.Long" -> PrimitiveGenerator.generateLong()
        "Float", "java.lang.Float" -> PrimitiveGenerator.generateFloat()
        "Double", "java.lang.Double" -> PrimitiveGenerator.generateDouble()
        "Byte", "java.lang.Byte" -> PrimitiveGenerator.generateByte()
        "Char", "java.lang.Character" -> PrimitiveGenerator.generateChar()
        "String", "java.lang.String" -> PrimitiveGenerator.generateString()
        "Instant", "java.time.Instant" -> JavaTimeGenerator.generateInstant()
        // Time
        "ZonedDateTime", "java.time.ZonedDateTime" -> JavaTimeGenerator.generateZoneDateTime()
        "ZoneId", "java.time.ZoneId" -> JavaTimeGenerator.generateZoneId()
        "LocalDate", "java.time.LocalDate" -> JavaTimeGenerator.generateLocalDate()
        "LocalDateTime", "java.time.LocalDateTime" -> JavaTimeGenerator.generateLocalDateTime()
        "LocalTime", "java.time.LocalTime" -> JavaTimeGenerator.generateLocalTime()
        "Period", "java.time.Period" -> JavaTimeGenerator.generatePeriod()
        // Library
        "UUID", "java.util.UUID" -> JavaLibraryGenerator.generateUUID()
        "BigDecimal", "java.math.BigDecimal" -> JavaLibraryGenerator.generateBigDecimal()
        "BigInteger", "java.math.BigInteger" -> JavaLibraryGenerator.generateBigInteger()
        // Collection
        "List", "java.util.List" -> JavaCollectionGenerator.generateList()
        "Iterable", "java.util.Iterable" -> JavaCollectionGenerator.generateIterable()
        else -> ClassGenerator.generateClass(context.asClassInstantiationContext())
    }
}

private fun handlePsiParameter(parameter: PsiParameter, context: PsiElementInstantiationStatementBuilderContext): InstantiationFieldBuilder? {
    val classType = PsiUtil.resolveClassInType(parameter.type)
    if (classType != null) return createInstantiationFieldIfPossible(context.copy(targetElement = TargetElement.of(classType)))

    return when (val type = parameter.type) {
        is PsiPrimitiveType -> when (type.name) {
            "int" -> PrimitiveGenerator.generateInteger()
            "float" -> PrimitiveGenerator.generateFloat()
            "double" -> PrimitiveGenerator.generateDouble()
            "long" -> PrimitiveGenerator.generateLong()
            "char" -> PrimitiveGenerator.generateChar()
            "short" -> PrimitiveGenerator.generateByte()
            "boolean" -> PrimitiveGenerator.generateBoolean()
            else -> null
        }
        else -> null
    }
}