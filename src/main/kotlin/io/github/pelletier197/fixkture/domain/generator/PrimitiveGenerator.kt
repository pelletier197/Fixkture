package io.github.pelletier197.fixkture.domain.generator

import com.intellij.psi.util.PsiUtil
import io.github.pelletier197.fixkture.domain.PsiElementInstantiationStatementBuilderContext
import io.github.pelletier197.fixkture.domain.InstantiationFieldBuilder
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.math.truncate
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
        return RegularCallbackClassInstantiationField { "'${charPool[Random.nextInt(0, charPool.size)]}'" }
    }

    fun generateString(context: PsiElementInstantiationStatementBuilderContext): InstantiationFieldBuilder {
        return RegularCallbackClassInstantiationField { "\"${toSnakeCase(context.targetElement.name)}\"" }
    }

    private fun toSnakeCase(value: String): String {
        return camelRegex.replace(value) {
            "_${it.value}"
        }.toLowerCase()
    }
}

