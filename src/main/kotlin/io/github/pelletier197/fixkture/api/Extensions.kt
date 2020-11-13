package io.github.pelletier197.fixkture.api

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Editor

val AnActionEvent.editor: Editor? get() = this.getData(CommonDataKeys.EDITOR)
