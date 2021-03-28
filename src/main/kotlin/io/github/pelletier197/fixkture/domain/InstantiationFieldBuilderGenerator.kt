package io.github.pelletier197.fixkture.domain

import com.intellij.psi.PsiArrayType
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiParameter
import com.intellij.psi.PsiPrimitiveType
import com.intellij.psi.PsiType
import com.intellij.psi.util.PsiUtil
import io.github.pelletier197.fixkture.domain.generator.LanguageCallbackInstantiationFieldBuilder
import io.github.pelletier197.fixkture.domain.generator.PrimitiveGenerator
import io.github.pelletier197.fixkture.domain.generator.java.ClassGenerator
import io.github.pelletier197.fixkture.domain.generator.java.JavaArrayGenerator
import io.github.pelletier197.fixkture.domain.generator.java.JavaCollectionGenerator
import io.github.pelletier197.fixkture.domain.generator.java.JavaEnumGenerator
import io.github.pelletier197.fixkture.domain.generator.java.JavaLibraryGenerator
import io.github.pelletier197.fixkture.domain.generator.java.JavaTimeGenerator

class NullInstantiationField : LanguageCallbackInstantiationFieldBuilder(
    java = { "null" },
    kotlin = { "null" }
)

data class FieldConstructionContext(
    val fieldName: String,
    val targetElement: TargetElement,
    val constructorSelector: ConstructorSelectionFunction,
    val interfaceImplementationSelector: InterfaceImplementationSelector,
    val variableNameGenerator: VariableNameGenerator,
) {
    fun asClassInstantiationStatementBuilderContext(element: TargetElement): PsiElementInstantiationStatementBuilderContext {
        return PsiElementInstantiationStatementBuilderContext(
            targetElement = element,
            constructorSelector = this.constructorSelector,
            interfaceImplementationSelector = this.interfaceImplementationSelector,
            variableNameGenerator = this.variableNameGenerator,
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
        is PsiType -> handlePsiType(element, context)
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
        // Other
        "UUID", "java.util.UUID" -> JavaLibraryGenerator.generateUUID()
        "BigDecimal", "java.math.BigDecimal" -> JavaLibraryGenerator.generateBigDecimal()
        "BigInteger", "java.math.BigInteger" -> JavaLibraryGenerator.generateBigInteger()
        // Collections
        "List", "java.util.List" -> JavaCollectionGenerator.generateList()
        "Set", "java.util.Set" -> JavaCollectionGenerator.generateSet()
        "HashSet", "java.util.HashSet" -> JavaCollectionGenerator.generateHashset()
        "TreeSet", "java.util.TreeSet" -> JavaCollectionGenerator.generateTreeSet()
        "ArrayList", "java.util.ArrayList" -> JavaCollectionGenerator.generateArrayList()
        "LinkedList", "java.util.LinkedList" -> JavaCollectionGenerator.generateLinkedList()
        "Iterable", "java.lang.Iterable" -> JavaCollectionGenerator.generateIterable()
        "Map", "java.util.Map" -> JavaCollectionGenerator.generateMap()
        "HashMap", "java.util.HashMap" -> JavaCollectionGenerator.generateHashMap()
        "TreeMap", "java.util.TreeMap" -> JavaCollectionGenerator.generateTreeMap()
        else -> when (element.isEnum) {
            true -> JavaEnumGenerator.generateEnum()
            else -> ClassGenerator.generateClass(context.asClassInstantiationContext(element))
        }
    }
}

fun handlePsiType(type: PsiType, context: PsiElementInstantiationStatementBuilderContext): InstantiationFieldBuilder? {
    return when (type) {
        is PsiPrimitiveType -> when (type.name) {
            "int" -> PrimitiveGenerator.generateInteger()
            "float" -> PrimitiveGenerator.generateFloat()
            "double" -> PrimitiveGenerator.generateDouble()
            "long" -> PrimitiveGenerator.generateLong()
            "char" -> PrimitiveGenerator.generateChar()
            "short" -> PrimitiveGenerator.generateByte()
            "boolean" -> PrimitiveGenerator.generateBoolean()
            "byte" -> PrimitiveGenerator.generateByte()
            else -> null
        }
        is PsiArrayType -> JavaArrayGenerator.generateArray()
        else -> when (val targetClass = PsiUtil.resolveClassInType(type)) {
            null -> null
            else -> handlePsiClass(targetClass, context)
        }
    }
}

private fun handlePsiParameter(parameter: PsiParameter, context: PsiElementInstantiationStatementBuilderContext): InstantiationFieldBuilder? {
    return handlePsiType(parameter.type, context)
}
