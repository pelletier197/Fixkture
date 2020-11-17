package io.github.pelletier197.fixkture.api

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.*
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.psi.codeStyle.JavaCodeStyleManager
import com.intellij.psi.util.PsiUtil
import com.intellij.refactoring.extractMethod.newImpl.ExtractMethodHelper.addSiblingAfter
import io.github.pelletier197.fixkture.api.java.selectTargetConstructor
import io.github.pelletier197.fixkture.api.java.selectTargetTargetClass
import io.github.pelletier197.fixkture.domain.ClassInstantiationStatementBuilderContext
import io.github.pelletier197.fixkture.domain.InstantiationStatementGenerator
import io.github.pelletier197.fixkture.domain.RecursiveClassInstantiationStatementGeneratorFactory
import org.jetbrains.kotlin.idea.core.ShortenReferences
import org.jetbrains.kotlin.idea.debugger.sequence.trace.dsl.KotlinStatementFactory
import org.jetbrains.kotlin.idea.formatter.KotlinStyleGuideCodeStyle
import org.jetbrains.kotlin.idea.kdoc.insert
import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtPsiFactory
import org.jetbrains.kotlin.psi.KtPsiUtil

class GenerateFixtureAction : AnAction() {
    override fun update(event: AnActionEvent) {
        event.presentation.isEnabledAndVisible = event.project != null
                && event.editor != null
                && event.file != null
                && event.file!!.isWritable
    }

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project!!

        val targetClass = selectTargetTargetClass(project) ?: return

        val statementGenerator = RecursiveClassInstantiationStatementGeneratorFactory().createInstantiationStatement(
                context = ClassInstantiationStatementBuilderContext(
                        targetClass = targetClass,
                        constructorSelector = { psiClass -> selectTargetConstructor(psiClass, project) }
                )
        )

        val statement = createStatement(event = event, generator = statementGenerator) ?: return

        val element = event.currentElement
        val file = event.file!!

        WriteCommandAction.runWriteCommandAction(project) {
            val addedElement = element?.addSiblingAfter(statement) ?: event.file!!.add(statement)

            if (event.file!!.isJava()) {
                JavaCodeStyleManager.getInstance(project).shortenClassReferences(addedElement)
            } else if (event.file!!.isKotlin()) {
                ShortenReferences.DEFAULT.process(file as KtFile)
            }
        }
    }

    private fun createStatement(event: AnActionEvent, generator: InstantiationStatementGenerator): PsiElement? {
        val project = event.project!!
        val file = event.file!!


        if (file.isJava()) {
            val factory = PsiElementFactory.getInstance(project)
            return factory.createStatementFromText(generator.createJavaStatement(), event.parentElement)
        }

        if (file.isKotlin()) {
            val factory = KtPsiFactory(project)
            return factory.createDeclaration(generator.createKotlinStatement())
        }

        return null
    }
}

