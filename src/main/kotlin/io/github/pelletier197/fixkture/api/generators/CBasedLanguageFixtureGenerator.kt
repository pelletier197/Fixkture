package io.github.pelletier197.fixkture.api.generators

import io.github.pelletier197.fixkture.domain.InstantiationStatementGenerator
import io.github.pelletier197.fixkture.domain.RecursiveClassInstantiationStatementGeneratorFactory

abstract class CBasedLanguageFixtureGenerator : FixtureGenerator {
    override fun generateFixture(context: FixtureGenerationContext) {
        val project = context.project
        val targetClass = selectTargetTargetClass(project) ?: return

        val statementGenerator = RecursiveClassInstantiationStatementGeneratorFactory(
            constructorSelector = { psiClass -> selectTargetConstructor(psiClass, project) },
            interfaceImplementationSelector = { psiClass -> selectInterfaceImplementationClass(psiClass, project) },
            variableNameGenerator = { psiClass -> suggestVariableName(psiClass, project) },
        ).createInstantiationStatement(
            psiClass = targetClass
        )

        generateFixture(
            statementGenerator = statementGenerator,
            context = context
        )
    }

    abstract fun generateFixture(statementGenerator: InstantiationStatementGenerator, context: FixtureGenerationContext)
}
