package io.github.pelletier197.fixkture.domain.generator

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiParameter
import com.intellij.psi.util.PsiUtil
import io.github.pelletier197.fixkture.domain.ClassInstantiationStatementBuilderContext
import io.github.pelletier197.fixkture.domain.InstantiationFieldBuilder
import io.github.pelletier197.fixkture.domain.createInstantiationField
import java.util.*

class NullInstantiationField : CallbackClassInstantiationFieldBuilder(
        LanguageCallbackValueGenerator(
                java = { "null" },
                kotlin = { "null" }
        )
)

class ClassInstantiationField(
        val targetClass: PsiClass,
        val targetConstructor: PsiMethod,
        val argumentsFields: List<InstantiationFieldBuilder>
) : CallbackClassInstantiationFieldBuilder(
        LanguageCallbackValueGenerator(
                java = { "" },
                kotlin = { "" }
        )
)


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
                targetConstructor,
                argumentsFields = instantiationFields
        )
    }

    private fun convertClassArgumentToInstantiationField(psiParameter: PsiParameter,
                                                         context: ClassInstantiationStatementBuilderContext
    ): InstantiationFieldBuilder {
        return when (val psiClass = PsiUtil.resolveClassInType(psiParameter.type)) {
            null -> NullInstantiationField()
            else -> createInstantiationField(context.copy(targetClass = psiClass))
        }
    }
}