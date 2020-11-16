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
import io.github.pelletier197.fixkture.domain.RecursiveClassInstantiationStatementGeneratorFactory
import org.jetbrains.kotlin.idea.kdoc.insert

class GenerateFixtureAction : AnAction() {
    override fun update(event: AnActionEvent) {
        event.presentation.isEnabledAndVisible = event.project != null && event.editor != null
    }

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project!!

        val targetClass = selectTargetTargetClass(project) ?: return
//        val targetConstructor = selectTargetConstructor(targetClass, project) ?: return
//
//
//        val statement = factory.createStatementFromText("public static final ${targetClass.qualifiedName} ${targetClass.name!!.decapitalize()} = new ${targetClass.qualifiedName}();", event.parentElement)

        val factory = PsiElementFactory.getInstance(project)
        val statementGenerator = RecursiveClassInstantiationStatementGeneratorFactory().createInstantiationStatement(
                context = ClassInstantiationStatementBuilderContext(
                        targetClass = targetClass,
                        constructorSelector = { psiClass -> selectTargetConstructor(psiClass, project) }
                )
        )
        val statement = factory.createStatementFromText(statementGenerator.createJavaStatement(), event.parentElement)

        val element = event.currentElement!!
        WriteCommandAction.runWriteCommandAction(project) {
            val addedElement = element.addSiblingAfter(statement)
            CodeStyleManager.getInstance(project).reformat(addedElement)
            JavaCodeStyleManager.getInstance(project).shortenClassReferences(addedElement)
        }
    }
}

