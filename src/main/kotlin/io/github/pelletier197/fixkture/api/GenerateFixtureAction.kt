package io.github.pelletier197.fixkture.api

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.psi.*
import com.intellij.psi.util.elementType
import com.intellij.util.DocumentUtil
import io.github.pelletier197.fixkture.api.java.selectTargetConstructor
import io.github.pelletier197.fixkture.api.java.selectTargetTargetClass

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
        val statement = factory.createVariableDeclarationStatement("name", targetType, factory.createExpressionFromText("${targetType.name.decapitalize()} = ${targetType.className}()", null))
        event.getData(CommonDataKeys.PSI_FILE)?.add(statement)

        TODO("Not yet implemented")
    }
}

