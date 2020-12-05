package io.github.pelletier197.fixkture.domain

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import io.github.pelletier197.fixkture.domain.generator.ClassInstantiationContext

typealias ConstructorSelectionFunction = (PsiClass) -> PsiMethod?

data class PsiElementInstantiationStatementBuilderContext(
        val targetElement: TargetElement,
        val constructorSelector: ConstructorSelectionFunction,
) {
    fun asClassInstantiationContext(): ClassInstantiationContext {
        return ClassInstantiationContext(
                targetClass = targetElement.element as PsiClass,
                constructorSelector = constructorSelector
        )
    }
}


class RecursiveClassInstantiationStatementGeneratorFactory(
        private val constructorSelector: ConstructorSelectionFunction
) {
    fun createInstantiationStatement(targetClass: PsiClass): InstantiationStatementGenerator {
        return generateInstantiationStatement(
                psiClass = targetClass,
                context = PsiElementInstantiationStatementBuilderContext(
                        targetElement = TargetElement.of(targetClass),
                        constructorSelector = constructorSelector
                )
        )
    }
}