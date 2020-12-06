package io.github.pelletier197.fixkture.domain.generator.java

import io.github.pelletier197.fixkture.domain.InstantiationFieldBuilder
import io.github.pelletier197.fixkture.domain.generator.LanguageCallbackInstantiationFieldBuilder

object JavaArrayGenerator {
    fun generateArray(): InstantiationFieldBuilder {
        return LanguageCallbackInstantiationFieldBuilder(
                java = { "" },
                kotlin = { "" }
        )
    }
}