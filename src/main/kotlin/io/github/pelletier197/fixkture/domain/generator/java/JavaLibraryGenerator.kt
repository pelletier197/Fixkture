package io.github.pelletier197.fixkture.domain.generator.java

import io.github.pelletier197.fixkture.domain.InstantiationFieldBuilder
import io.github.pelletier197.fixkture.domain.generator.RegularCallbackClassInstantiationField
import kotlin.math.truncate
import kotlin.random.Random

object JavaLibraryGenerator {
    fun generateUUID(): InstantiationFieldBuilder {
        return RegularCallbackClassInstantiationField { "java.util.UUID.randomUUID()" }
    }

    fun generateBigDecimal(): InstantiationFieldBuilder {
        return RegularCallbackClassInstantiationField { "java.math.BigDecimal.valueOf(${truncate(Random.nextDouble(50_000.0, 500_000.0))})" }
    }

    fun generateBigInteger(): InstantiationFieldBuilder {
        return RegularCallbackClassInstantiationField { "java.math.BigInteger.valueOf(${Random.nextInt(50_000, 500_000)})" }
    }
}