package io.github.pelletier197.fixkture.api.generators

import com.intellij.openapi.actionSystem.AnActionEvent
import io.github.pelletier197.fixkture.api.currentElement
import io.github.pelletier197.fixkture.api.currentProject
import io.github.pelletier197.fixkture.api.file
import io.github.pelletier197.fixkture.domain.ClassInstantiationStatementBuilderContext
import io.github.pelletier197.fixkture.domain.InstantiationStatementGenerator
import io.github.pelletier197.fixkture.domain.RecursiveClassInstantiationStatementGeneratorFactory

abstract class CBasedLanguageFixtureGenerator : FixtureGenerator {
    override fun generateFixture(context: FixtureGenerationContext) {
        val project = context.project
        val targetClass = selectTargetTargetClass(project) ?: return

        val statementGenerator = RecursiveClassInstantiationStatementGeneratorFactory().createInstantiationStatement(
                context = ClassInstantiationStatementBuilderContext(
                        targetClass = targetClass,
                        constructorSelector = { psiClass -> selectTargetConstructor(psiClass, project) }
                )
        )

        generateFixture(
                statementGenerator = statementGenerator,
                context = context
        )
    }

    abstract fun generateFixture(statementGenerator: InstantiationStatementGenerator, context: FixtureGenerationContext)
}