package io.github.pelletier197.fixkture.api

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiJavaFile
import org.jetbrains.kotlin.psi.KtFile

val AnActionEvent.currentProject: Project get() = currentProjectOrNull() ?: error("project should not be null")
val AnActionEvent.editor: Editor get() = editorOrNull() ?: error("editor should not be null")
val AnActionEvent.file: PsiFile get() = fileOrNull() ?: error("file should not be null")
val AnActionEvent.caret: Caret get() = caretOrNull() ?: error("caret should not be null")

fun AnActionEvent.currentProjectOrNull(): Project? = this.getData(CommonDataKeys.PROJECT)
fun AnActionEvent.editorOrNull(): Editor? = this.getData(CommonDataKeys.EDITOR)
fun AnActionEvent.fileOrNull(): PsiFile? = this.getData(CommonDataKeys.PSI_FILE)
fun AnActionEvent.caretOrNull(): Caret? = this.getData(CommonDataKeys.CARET)

val AnActionEvent.currentElement: PsiElement? get() = caretOrNull()?.let { fileOrNull()?.findElementAt(it.caretModel.offset) }
val AnActionEvent.parentElement: PsiElement? get() = this.currentElement?.parent

fun PsiFile.isJava(): Boolean {
    return this is PsiJavaFile
}

fun PsiFile.isKotlin(): Boolean {
    return this is KtFile
}

fun PsiFile.isSupported(): Boolean {
    return isWritable && (isKotlin() || isJava())
}
