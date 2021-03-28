package io.github.pelletier197.fixkture.domain

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import io.github.pelletier197.fixkture.domain.generator.java.ClassInstantiationContext

typealias ConstructorSelectionFunction = (PsiClass) -> PsiMethod?
typealias InterfaceImplementationSelector = (PsiClass) -> PsiClass?
typealias VariableNameGenerator = (PsiClass) -> String

data class PsiElementInstantiationStatementBuilderContext(
    val targetElement: TargetElement,
    val constructorSelector: ConstructorSelectionFunction,
    val interfaceImplementationSelector: InterfaceImplementationSelector,
    val variableNameGenerator: VariableNameGenerator,
) {
    fun asClassInstantiationContext(targetClass: PsiClass): ClassInstantiationContext {
        return ClassInstantiationContext(
            targetClass = targetClass,
            constructorSelector = constructorSelector,
            interfaceImplementationSelector = interfaceImplementationSelector,
            variableNameGenerator = variableNameGenerator,
        )
    }
}

class RecursiveClassInstantiationStatementGeneratorFactory(
    private val constructorSelector: ConstructorSelectionFunction,
    private val interfaceImplementationSelector: InterfaceImplementationSelector,
    private val variableNameGenerator: VariableNameGenerator,
) {
    fun createInstantiationStatement(psiClass: PsiClass): InstantiationStatementGenerator {
        return generateInstantiationStatement(
            element = psiClass,
            context = PsiElementInstantiationStatementBuilderContext(
                targetElement = TargetElement.of(psiClass),
                constructorSelector = constructorSelector,
                interfaceImplementationSelector = interfaceImplementationSelector,
                variableNameGenerator = variableNameGenerator,
            )
        )
    }
}
