package io.github.pelletier197.fixkture.domain

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import io.github.pelletier197.fixkture.domain.generator.ClassInstantiationContext

data class PsiElementInstantiationStatementBuilderContext(
        val targetElement: PsiElement,
        val constructorSelector: (PsiClass) -> PsiMethod?,
) {
    fun asClassInstantiationContext(): ClassInstantiationContext {
        return ClassInstantiationContext(
                targetClass = targetElement as PsiClass,
                constructorSelector = constructorSelector
        )
    }
}


class RecursiveClassInstantiationStatementGeneratorFactory {
    fun createInstantiationStatement(context: PsiElementInstantiationStatementBuilderContext): InstantiationStatementGenerator {
        return generateInstantiationStatement(context)
    }
}