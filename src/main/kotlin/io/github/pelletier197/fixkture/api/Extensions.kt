package io.github.pelletier197.fixkture.api

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiJavaFile
import org.jetbrains.kotlin.psi.KtFile

val AnActionEvent.editor: Editor? get() = this.getData(CommonDataKeys.EDITOR)
val AnActionEvent.file: PsiFile? get() = this.getData(CommonDataKeys.PSI_FILE)
val AnActionEvent.caret: Caret? get() = this.getData(CommonDataKeys.CARET)
val AnActionEvent.currentElement: PsiElement? get() = caret?.let { file?.findElementAt(it.caretModel.offset) }
val AnActionEvent.parentElement: PsiElement? get() = this.currentElement?.parent

fun PsiFile.isJava(): Boolean {
    return this is PsiJavaFile
}

fun PsiFile.isKotlin() : Boolean {
    return this is KtFile
}