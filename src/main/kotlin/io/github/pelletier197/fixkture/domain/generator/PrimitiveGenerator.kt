package io.github.pelletier197.fixkture.domain.generator

import io.github.pelletier197.fixkture.domain.InstantiationFieldBuilder
import kotlin.random.Random

object PrimitiveGenerator {
    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    private val camelRegex = "(?<=[a-zA-Z])[A-Z]".toRegex()

    fun generateBoolean(): InstantiationFieldBuilder {
        return ConstantCallbackClassInstantiationField { Random.nextBoolean().toString() }
    }

    fun generateInteger(): InstantiationFieldBuilder {
        return ConstantCallbackClassInstantiationField { Random.nextInt(0, 5000).toString() }
    }

    fun generateLong(): InstantiationFieldBuilder {
        return ConstantCallbackClassInstantiationField { Random.nextLong(0, 100_000).toString() }
    }

    fun generateFloat(): InstantiationFieldBuilder {
        return ConstantCallbackClassInstantiationField { Random.nextFloat().toString() }
    }

    fun generateDouble(): InstantiationFieldBuilder {
        return ConstantCallbackClassInstantiationField { Random.nextDouble().toString() }
    }

    fun generateByte(): InstantiationFieldBuilder {
        return ConstantCallbackClassInstantiationField { Random.nextBytes(1)[0].toString() }
    }

    fun generateChar(): InstantiationFieldBuilder {
        return ConstantCallbackClassInstantiationField { "'${charPool[Random.nextInt(0, charPool.size)]}'" }
    }

    fun generateString(): InstantiationFieldBuilder {
        return ConstantCallbackClassInstantiationField { context -> "\"${toSnakeCase(context.fieldName)}\"" }
    }

    private fun toSnakeCase(value: String): String {
        return camelRegex.replace(value) {
            "_${it.value}"
        }.toLowerCase()
    }
}
