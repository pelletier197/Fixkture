package io.github.pelletier197.fixkture.domain.generator

import io.github.pelletier197.fixkture.domain.InstantiationFieldBuilder

object JavaTimeGenerator {
    fun generateInstant() : InstantiationFieldBuilder {
        return RegularCallbackClassInstantiationField() {
            ""
        }
    }
}