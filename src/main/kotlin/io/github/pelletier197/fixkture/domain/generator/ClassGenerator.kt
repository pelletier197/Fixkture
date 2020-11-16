package io.github.pelletier197.fixkture.domain.generator

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiParameter
import com.intellij.psi.util.PsiUtil
import io.github.pelletier197.fixkture.domain.ClassInstantiationStatementBuilderContext
import io.github.pelletier197.fixkture.domain.FieldConstructionContext
import io.github.pelletier197.fixkture.domain.InstantiationFieldBuilder
import io.github.pelletier197.fixkture.domain.createInstantiationField

class NullInstantiationField : CallbackClassInstantiationFieldBuilder(
        LanguageCallbackValueGenerator(
                java = { "null" },
                kotlin = { "null" }
        )
)

class ClassInstantiationField(
        val targetClass: PsiClass,
        val argumentsFields: List<InstantiationFieldBuilder>
) : CallbackClassInstantiationFieldBuilder(
        LanguageCallbackValueGenerator(
                java = { generateJavaClass(targetClass = targetClass, arguments = argumentsFields, context = it) },
                kotlin = { generateKotlinClass(targetClass = targetClass, arguments = argumentsFields, context = it) }
        )
)

private fun generateJavaClass(targetClass: PsiClass, arguments: List<InstantiationFieldBuilder>, context: FieldConstructionContext): String {
    val parameters = arguments.joinToString(separator = ", ") { it.asJavaConstructorArgument(context) }
    return "new ${targetClass.qualifiedName}($parameters)"
}

private fun generateKotlinClass(targetClass: PsiClass, arguments: List<InstantiationFieldBuilder>, context: FieldConstructionContext): String {
    val parameters = arguments.joinToString(separator = ", ") { it.asKotlinConstructorArgument(context) }
    return "${targetClass.qualifiedName}($parameters)"
}

object ClassGenerator {
    fun generateClass(context: ClassInstantiationStatementBuilderContext): InstantiationFieldBuilder {
        val targetClass = context.targetClass
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

    private fun convertClassArgumentToInstantiationField(psiParameter: PsiParameter,
                                                         context: ClassInstantiationStatementBuilderContext
    ): InstantiationFieldBuilder {
        // TODO - this should support for list creation, array creation, etc.. not use PSiUtil.resolveClassInType
        return when (val psiClass = PsiUtil.resolveClassInType(psiParameter.type)) {
            null -> NullInstantiationField()
            else -> createInstantiationField(context.copy(targetClass = psiClass))
        }
    }
}