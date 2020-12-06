package io.github.pelletier197.fixkture.api

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.psi.PsiFile
import io.github.pelletier197.fixkture.api.generators.FixtureGenerationContext
import io.github.pelletier197.fixkture.api.generators.FixtureGenerator
import io.github.pelletier197.fixkture.api.generators.java.JavaIntellijFixtureGenerator
import io.github.pelletier197.fixkture.api.generators.kotlin.KotlinIntellijFixtureGenerator

class GenerateFixtureAction : AnAction() {
    override fun update(event: AnActionEvent) {
        event.presentation.isEnabledAndVisible = event.project != null &&
            event.editorOrNull() != null &&
            event.fileOrNull() != null &&
            event.file.isSupported()
    }

    override fun actionPerformed(event: AnActionEvent) {
        getGenerator(event.file)?.generateFixture(
            context = FixtureGenerationContext(
                project = event.currentProject,
                caret = event.caret,
                file = event.file
            )
        )
    }

    private fun getGenerator(file: PsiFile): FixtureGenerator? {
        if (file.isJava()) return JavaIntellijFixtureGenerator()
        if (file.isKotlin()) return KotlinIntellijFixtureGenerator()
        return null
    }
}
