package io.github.pelletier197.fixkture.domain

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod

data class ClassInstantiationStatementBuilderContext(
        val targetClass: PsiClass,
        val constructorSelector: (PsiClass) -> PsiMethod?,
)


class RecursiveClassInstantiationStatementGeneratorFactory {
    fun createInstantiationStatement(context: ClassInstantiationStatementBuilderContext): InstantiationStatementGenerator {
        return generateInstantiationStatement(context)
    }
}