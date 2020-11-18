package io.github.pelletier197.fixkture.api.generators

import com.intellij.openapi.actionSystem.AnActionEvent

interface FixtureGenerator {
    fun generateFixture(context: FixtureGenerationContext)
}