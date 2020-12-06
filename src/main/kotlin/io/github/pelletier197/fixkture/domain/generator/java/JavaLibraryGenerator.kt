package io.github.pelletier197.fixkture.domain.generator.java

import io.github.pelletier197.fixkture.domain.InstantiationFieldBuilder
import io.github.pelletier197.fixkture.domain.generator.ConstantCallbackClassInstantiationField
import kotlin.math.truncate
import kotlin.random.Random

object JavaLibraryGenerator {
    fun generateUUID(): InstantiationFieldBuilder {
        return ConstantCallbackClassInstantiationField { "java.util.UUID.randomUUID()" }
    }

    fun generateBigDecimal(): InstantiationFieldBuilder {
        return ConstantCallbackClassInstantiationField { "java.math.BigDecimal.valueOf(${truncate(Random.nextDouble(50_000.0, 500_000.0))})" }
    }

    fun generateBigInteger(): InstantiationFieldBuilder {
        return ConstantCallbackClassInstantiationField { "java.math.BigInteger.valueOf(${Random.nextInt(50_000, 500_000)})" }
    }
}
