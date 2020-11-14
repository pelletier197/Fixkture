package io.github.pelletier197.fixkture.domain.generator

import io.github.pelletier197.fixkture.domain.ClassInstantiationStatementBuilderContext
import io.github.pelletier197.fixkture.domain.InstantiationFieldBuilder
import kotlin.random.Random

object PrimitiveGenerator {
    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    private val camelRegex = "(?<=[a-zA-Z])[A-Z]".toRegex()

    fun generateBoolean(): InstantiationFieldBuilder {
        return RegularCallbackClassInstantiationField { Random.nextBoolean().toString() }
    }

    fun generateInteger(): InstantiationFieldBuilder {
        return RegularCallbackClassInstantiationField { Random.nextInt(0, 5000).toString() }
    }

    fun generateLong(): InstantiationFieldBuilder {
        return RegularCallbackClassInstantiationField { Random.nextLong(0, 100_000).toString() }
    }

    fun generateFloat(): InstantiationFieldBuilder {
        return RegularCallbackClassInstantiationField { Random.nextFloat().toString() }
    }

    fun generateDouble(): InstantiationFieldBuilder {
        return RegularCallbackClassInstantiationField { Random.nextDouble().toString() }
    }

    fun generateByte(): InstantiationFieldBuilder {
        return RegularCallbackClassInstantiationField { Random.nextBytes(1)[0].toString() }
    }

    fun generateChar(): InstantiationFieldBuilder {
        return RegularCallbackClassInstantiationField { "'${Random.nextInt(0, charPool.size).toChar()}'" }
    }

    fun generateString(context: ClassInstantiationStatementBuilderContext): InstantiationFieldBuilder {
        return RegularCallbackClassInstantiationField { "\"${toSnakeCase(context.targetClass.name!!)}\"" }
    }

    fun generateUUID(): InstantiationFieldBuilder {
        return RegularCallbackClassInstantiationField { "java.util.UUID.randomUUID()" }
    }

    private fun toSnakeCase(value: String): String {
        return camelRegex.replace(value) {
            "_${it.value}"
        }.toLowerCase()
    }
}