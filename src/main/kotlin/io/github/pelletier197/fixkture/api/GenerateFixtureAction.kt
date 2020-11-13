package io.github.pelletier197.fixkture.api

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.CommandProcessor
import com.intellij.psi.*
import com.intellij.psi.codeStyle.JavaCodeStyleManager
import com.intellij.psi.util.PsiUtil
import com.intellij.refactoring.extractMethod.newImpl.ExtractMethodHelper.addSiblingAfter
import io.github.pelletier197.fixkture.api.java.selectTargetConstructor
import io.github.pelletier197.fixkture.api.java.selectTargetTargetClass
import org.jetbrains.kotlin.idea.kdoc.insert

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
        val statement = factory.createVariableDeclarationStatement(targetType.name.decapitalize(), targetType, factory.createExpressionFromText("new ${targetClass.qualifiedName}()", null))
        PsiUtil.setModifierProperty(statement.declaredElements[0] as PsiVariable, PsiModifier.FINAL, true)
        PsiUtil.setModifierProperty(statement.declaredElements[0] as PsiVariable, PsiModifier.PUBLIC, true)
        PsiUtil.setModifierProperty(statement.declaredElements[0] as PsiVariable, PsiModifier.STATIC, true)


        CommandProcessor.getInstance().executeCommand(project, {
            val element = event.getData(CommonDataKeys.PSI_FILE)?.findElementAt(event.getData(CommonDataKeys.CARET)!!.caretModel.offset)!!
            element.addSiblingAfter(statement)
            JavaCodeStyleManager.getInstance(project).shortenClassReferences(element)
        }, "Generate fixture", null)
    }
}

