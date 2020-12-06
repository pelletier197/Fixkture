package io.github.pelletier197.fixkture.api.generators

interface FixtureGenerator {
    fun generateFixture(context: FixtureGenerationContext)
}
