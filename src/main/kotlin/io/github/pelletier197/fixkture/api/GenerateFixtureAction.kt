package io.github.pelletier197.fixkture.api

import com.intellij.ide.util.TreeClassChooserFactory
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class GenerateFixtureAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val classChooserFactory = TreeClassChooserFactory.getInstance(event.project)
        val classChooser = classChooserFactory.createAllProjectScopeChooser("Select a target class for you fixture")
        classChooser.showDialog()
        val selectedClass = classChooser.selected
        TODO("Not yet implemented")
    }
}

