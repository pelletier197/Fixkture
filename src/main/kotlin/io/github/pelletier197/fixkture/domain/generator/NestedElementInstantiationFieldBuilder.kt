package io.github.pelletier197.fixkture.domain.generator

import io.github.pelletier197.fixkture.domain.FieldConstructionContext
import io.github.pelletier197.fixkture.domain.InstantiationFieldBuilder
import io.github.pelletier197.fixkture.domain.TargetElement

class NestedElementInstantiationFieldBuilder(
    private val elementBuilder: InstantiationFieldBuilder,
    private val targetElement: TargetElement,
) : InstantiationFieldBuilder {
    override fun asJavaConstructorArgument(context: FieldConstructionContext): String {
        return elementBuilder.asJavaConstructorArgument(context.copy(targetElement = targetElement))
    }

    override fun asKotlinConstructorArgument(context: FieldConstructionContext): String {
        return elementBuilder.asKotlinConstructorArgument(context.copy(targetElement = targetElement))
    }

    override fun asJavaFlatValue(context: FieldConstructionContext): String {
        return elementBuilder.asJavaFlatValue(context.copy(targetElement = targetElement))
    }

    override fun asKotlinFlatValue(context: FieldConstructionContext): String {
        return elementBuilder.asKotlinFlatValue(context.copy(targetElement = targetElement))
    }
}
