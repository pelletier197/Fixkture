package io.github.pelletier197.fixkture.api.generators

import com.intellij.openapi.editor.Caret
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

data class FixtureGenerationContext(
    val project: Project,
    val caret: Caret?,
    val file: PsiFile,
) {
    val currentElement: PsiElement? get() = caret?.let { file.findElementAt(it.caretModel.offset) }
    val parentElement: PsiElement? get() = this.currentElement?.parent
}
