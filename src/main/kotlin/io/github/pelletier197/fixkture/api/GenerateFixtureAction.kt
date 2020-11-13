package io.github.pelletier197.fixkture.api

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.*
import com.intellij.psi.codeStyle.JavaCodeStyleManager
import com.intellij.psi.util.PsiUtil
import com.intellij.psi.util.elementType
import com.intellij.util.DocumentUtil
import io.github.pelletier197.fixkture.api.java.selectTargetConstructor
import io.github.pelletier197.fixkture.api.java.selectTargetTargetClass
import org.jetbrains.kotlin.idea.highlighter.KotlinPsiChecker
import org.jetbrains.kotlin.resolve.jvm.KotlinJavaPsiFacade

class GenerateFixtureAction : AnAction() {
    override fun update(event: AnActionEvent) {
        event.presentation.isEnabledAndVisible = event.project != null && event.editor != null
    }

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project!!

        val targetClass = selectTargetTargetClass(project) ?: return
        val targetConstructor = selectTargetConstructor(targetClass, project) ?: return

        val factory = PsiElementFactory.getInstance(project)

        val targetType = JavaPsiFacade.getElementFactory(project).createType(targetClass)
        val statement = factory.createVariableDeclarationStatement(targetType.name.decapitalize(), targetType, factory.createExpressionFromText("${targetType.name}()", null))
        PsiUtil.setModifierProperty(statement.declaredElements[0] as PsiVariable, PsiModifier.FINAL, true)


        CommandProcessor.getInstance().executeCommand(project, {
            val file = event.getData(CommonDataKeys.PSI_FILE)!!
            file.add(statement)
            JavaCodeStyleManager.getInstance(project).shortenClassReferences(statement)
        }, "Generate fixture", null)

    }
}

