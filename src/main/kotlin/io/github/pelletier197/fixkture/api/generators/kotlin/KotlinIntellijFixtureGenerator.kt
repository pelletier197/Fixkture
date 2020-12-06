package io.github.pelletier197.fixkture.api.generators.kotlin

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.refactoring.extractMethod.newImpl.ExtractMethodHelper.addSiblingAfter
import io.github.pelletier197.fixkture.api.generators.CBasedLanguageFixtureGenerator
import io.github.pelletier197.fixkture.api.generators.FixtureGenerationContext
import io.github.pelletier197.fixkture.domain.InstantiationStatementGenerator
import org.jetbrains.kotlin.idea.core.ShortenReferences
import org.jetbrains.kotlin.psi.KtDeclaration
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtPsiFactory

class KotlinIntellijFixtureGenerator : CBasedLanguageFixtureGenerator() {
    override fun generateFixture(statementGenerator: InstantiationStatementGenerator, context: FixtureGenerationContext) {
        val project = context.project
        val statement = createStatement(project = project, generator = statementGenerator)

        val element = context.currentElement
        val file = context.file as KtFile

        WriteCommandAction.runWriteCommandAction(project) {
            element?.addSiblingAfter(statement) ?: file.add(statement)
            ShortenReferences.DEFAULT.process(file)
        }
    }

    private fun createStatement(generator: InstantiationStatementGenerator, project: Project): KtDeclaration {
        val factory = KtPsiFactory(project)
        return factory.createDeclaration(generator.createKotlinStatement())
    }
}
