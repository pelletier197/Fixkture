package io.github.pelletier197.fixkture.domain.generator

import io.github.pelletier197.fixkture.domain.FieldConstructionContext
import io.github.pelletier197.fixkture.domain.InstantiationFieldBuilder


typealias ClassFieldGeneratorCallback = (FieldConstructionContext) -> String

data class LanguageCallbackValueGenerator(
        val java: ClassFieldGeneratorCallback,
        val kotlin: ClassFieldGeneratorCallback,
)


open class CallbackClassInstantiationFieldBuilder(
        private val callbacks: LanguageCallbackValueGenerator
) : InstantiationFieldBuilder {
    override fun asJavaConstructorArgument(context: FieldConstructionContext): String {
        // new Class(<target>)
        return callbacks.java(context)
    }

    override fun asKotlinConstructorArgument(context: FieldConstructionContext): String {
        // Class(<constructorFieldName = target>)
        return "${context.fieldName} = ${callbacks.kotlin(context)}"
    }

    override fun asJavaFlatValue(context: FieldConstructionContext): String {
        // final Target variable = <Target>
        return callbacks.java(context)
    }

    override fun asKotlinFlatValue(context: FieldConstructionContext): String {
        // val Target = <Target>
        return callbacks.kotlin(context)
    }
}

class RegularCallbackClassInstantiationField(
        callback: ClassFieldGeneratorCallback
) : CallbackClassInstantiationFieldBuilder(
        LanguageCallbackValueGenerator(
                java = callback,
                kotlin = callback
        )
)

