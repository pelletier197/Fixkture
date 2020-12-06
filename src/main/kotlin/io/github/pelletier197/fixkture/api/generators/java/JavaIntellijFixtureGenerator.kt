package io.github.pelletier197.fixkture.api.generators.java

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiElementFactory
import com.intellij.psi.PsiStatement
import com.intellij.psi.codeStyle.JavaCodeStyleManager
import com.intellij.refactoring.extractMethod.newImpl.ExtractMethodHelper.addSiblingAfter
import io.github.pelletier197.fixkture.api.generators.CBasedLanguageFixtureGenerator
import io.github.pelletier197.fixkture.api.generators.FixtureGenerationContext
import io.github.pelletier197.fixkture.domain.InstantiationStatementGenerator

class JavaIntellijFixtureGenerator : CBasedLanguageFixtureGenerator() {
    override fun generateFixture(statementGenerator: InstantiationStatementGenerator, context: FixtureGenerationContext) {
        val project = context.project
        val statement = createStatement(context = context, generator = statementGenerator)

        val element = context.currentElement
        val file = context.file

        WriteCommandAction.runWriteCommandAction(project) {
            val addedElement = element?.addSiblingAfter(statement) ?: file.add(statement)
            JavaCodeStyleManager.getInstance(project).shortenClassReferences(addedElement)
        }
    }

    private fun createStatement(generator: InstantiationStatementGenerator, context: FixtureGenerationContext): PsiStatement {
        val factory = PsiElementFactory.getInstance(context.project)
        return factory.createStatementFromText(generator.createJavaStatement(), context.parentElement)
    }
}
