package io.github.pelletier197.fixkture.domain.generator.java

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiParameter
import com.intellij.psi.util.PsiUtil
import io.github.pelletier197.fixkture.domain.ConstructorSelectionFunction
import io.github.pelletier197.fixkture.domain.FieldConstructionContext
import io.github.pelletier197.fixkture.domain.InstantiationFieldBuilder
import io.github.pelletier197.fixkture.domain.InterfaceImplementationSelector
import io.github.pelletier197.fixkture.domain.NullInstantiationField
import io.github.pelletier197.fixkture.domain.PsiElementInstantiationStatementBuilderContext
import io.github.pelletier197.fixkture.domain.TargetElement
import io.github.pelletier197.fixkture.domain.VariableNameGenerator
import io.github.pelletier197.fixkture.domain.createInstantiationField
import io.github.pelletier197.fixkture.domain.generator.LanguageCallbackInstantiationFieldBuilder
import org.jetbrains.kotlin.asJava.classes.KtUltraLightClass

open class ClassParameterInstantiationField(
    private val parameter: PsiParameter,
    private val instantiationField: InstantiationFieldBuilder
) : InstantiationFieldBuilder {
    override fun asJavaConstructorArgument(context: FieldConstructionContext): String {
        return instantiationField.asJavaConstructorArgument(modifyContext(context))
    }

    override fun asKotlinConstructorArgument(context: FieldConstructionContext): String {
        return instantiationField.asKotlinConstructorArgument(modifyContext(context))
    }

    override fun asJavaFlatValue(context: FieldConstructionContext): String {
        return instantiationField.asJavaFlatValue(modifyContext(context))
    }

    override fun asKotlinFlatValue(context: FieldConstructionContext): String {
        return instantiationField.asKotlinFlatValue(modifyContext(context))
    }

    private fun modifyContext(context: FieldConstructionContext): FieldConstructionContext {
        return context.copy(
            fieldName = parameter.name,
            targetElement = TargetElement.of(parameter),
        )
    }
}

class NullClassArgumentInstantiationField(
    parameter: PsiParameter
) : ClassParameterInstantiationField(
    parameter = parameter,
    instantiationField = NullInstantiationField()
)

data class ClassInstantiationContext(
    val targetClass: PsiClass,
    val constructorSelector: ConstructorSelectionFunction,
    val interfaceImplementationSelector: InterfaceImplementationSelector,
    val variableNameGenerator: VariableNameGenerator,
) {
    fun asClassInstantiationStatementBuilderContext(element: PsiElement): PsiElementInstantiationStatementBuilderContext {
        return PsiElementInstantiationStatementBuilderContext(
            targetElement = TargetElement.of(element),
            constructorSelector = constructorSelector,
            interfaceImplementationSelector = interfaceImplementationSelector,
            variableNameGenerator = variableNameGenerator
        )
    }

    fun forClass(targetClass: PsiClass): ClassInstantiationContext {
        return copy(targetClass = targetClass)
    }
}

class ClassInstantiationField(
    val targetClass: PsiClass,
    val argumentsFields: List<InstantiationFieldBuilder>
) : LanguageCallbackInstantiationFieldBuilder(
    java = { generateJavaClass(targetClass = targetClass, arguments = argumentsFields, context = it) },
    kotlin = { generateKotlinClass(targetClass = targetClass, arguments = argumentsFields, context = it) }
)

private fun generateJavaClass(targetClass: PsiClass, arguments: List<InstantiationFieldBuilder>, context: FieldConstructionContext): String {
    val parameters = arguments.joinToString(separator = ", ") { it.asJavaConstructorArgument(context) }
    return "new ${targetClass.qualifiedName}($parameters)"
}

private fun generateKotlinClass(targetClass: PsiClass, arguments: List<InstantiationFieldBuilder>, context: FieldConstructionContext): String {
    val parameters = getKotlinParametersString(targetClass, arguments, context)
    return "${targetClass.qualifiedName}($parameters)"
}

private fun getKotlinParametersString(targetClass: PsiClass, arguments: List<InstantiationFieldBuilder>, context: FieldConstructionContext): String {
    // The target class is also a kotlin class
    if (targetClass is KtUltraLightClass) {
        return arguments.joinToString(separator = ", ") { it.asKotlinConstructorArgument(context) }
    }

    // The target class is a Java class
    return arguments.joinToString(separator = ", ") { it.asKotlinFlatValue(context) }
}

object ClassGenerator {
    fun generateClass(context: ClassInstantiationContext): InstantiationFieldBuilder {
        val targetClass = context.targetClass
        if (targetClass.isInterface) return handleInterfaceType(context)
        val targetConstructor = context.constructorSelector(targetClass) ?: return NullInstantiationField()

        val arguments = targetConstructor.parameterList.parameters.toList()
        val instantiationFields = arguments.map {
            convertClassArgumentToInstantiationField(psiParameter = it, context = context)
        }

        return ClassInstantiationField(
            targetClass = targetClass,
            argumentsFields = instantiationFields
        )
    }

    private fun handleInterfaceType(context: ClassInstantiationContext): InstantiationFieldBuilder {
        val implementation =
            context.interfaceImplementationSelector(context.targetClass) ?: return NullInstantiationField()
        return generateClass(
            context.forClass(targetClass = implementation)
        )
    }

    private fun convertClassArgumentToInstantiationField(
        psiParameter: PsiParameter,
        context: ClassInstantiationContext
    ): InstantiationFieldBuilder {
        if (isRecursive(psiParameter, context)) return NullClassArgumentInstantiationField(psiParameter)

        return ClassParameterInstantiationField(
            parameter = psiParameter,
            instantiationField = createInstantiationField(
                context = context.asClassInstantiationStatementBuilderContext(psiParameter)
            )
        )
    }

    private fun isRecursive(psiParameter: PsiParameter, context: ClassInstantiationContext): Boolean {
        // To avoid stack-overflow when generating the same object recursively
        val targetParameterClass = PsiUtil.resolveClassInType(psiParameter.type)
        return targetParameterClass == context.targetClass
    }
}
