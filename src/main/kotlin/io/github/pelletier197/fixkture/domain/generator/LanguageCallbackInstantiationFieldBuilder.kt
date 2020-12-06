package io.github.pelletier197.fixkture.domain.generator

import io.github.pelletier197.fixkture.domain.FieldConstructionContext
import io.github.pelletier197.fixkture.domain.InstantiationFieldBuilder

typealias ClassFieldGeneratorCallback = (FieldConstructionContext) -> String

open class LanguageCallbackInstantiationFieldBuilder(
    val java: ClassFieldGeneratorCallback,
    val kotlin: ClassFieldGeneratorCallback,
) : InstantiationFieldBuilder {
    override fun asJavaConstructorArgument(context: FieldConstructionContext): String {
        // new Class(<target>)
        return java(context)
    }

    override fun asKotlinConstructorArgument(context: FieldConstructionContext): String {
        // Class(<constructorFieldName = target>)
        return "${context.fieldName} = ${kotlin(context)}"
    }

    override fun asJavaFlatValue(context: FieldConstructionContext): String {
        // final Target variable = <Target>
        return java(context)
    }

    override fun asKotlinFlatValue(context: FieldConstructionContext): String {
        // val Target = <Target>
        return kotlin(context)
    }
}

class ConstantCallbackClassInstantiationField(
    callback: ClassFieldGeneratorCallback
) : LanguageCallbackInstantiationFieldBuilder(
    java = callback,
    kotlin = callback
)
