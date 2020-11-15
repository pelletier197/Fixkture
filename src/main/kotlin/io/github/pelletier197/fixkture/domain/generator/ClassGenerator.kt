package io.github.pelletier197.fixkture.domain.generator

import io.github.pelletier197.fixkture.domain.ClassInstantiationStatementBuilderContext
import io.github.pelletier197.fixkture.domain.InstantiationFieldBuilder

object ClassGenerator {
    fun generateClass(context: ClassInstantiationStatementBuilderContext): InstantiationFieldBuilder {
        val targetClass = context.targetClass
        val targetConstructor = context.constructorSelector(targetClass) ?: NullInstantiationField()

    }
}