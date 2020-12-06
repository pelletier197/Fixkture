package io.github.pelletier197.fixkture.domain.generator.java

import io.github.pelletier197.fixkture.domain.InstantiationFieldBuilder
import io.github.pelletier197.fixkture.domain.generator.ConstantCallbackClassInstantiationField
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Period
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import kotlin.random.Random

object JavaTimeGenerator {
    fun generateInstant(): InstantiationFieldBuilder {
        return ConstantCallbackClassInstantiationField {
            "java.time.Instant.parse(\"${generateRandomInstant()}\")"
        }
    }

    fun generateZoneDateTime(): InstantiationFieldBuilder {
        return ConstantCallbackClassInstantiationField {
            "java.time.ZonedDateTime.parse(\"${generateRandomInstant().atZone(generateRandomZoneId())}\")"
        }
    }

    fun generateZoneId(): InstantiationFieldBuilder {
        return ConstantCallbackClassInstantiationField {
            "java.time.ZoneId.of(\"${generateRandomZoneId().id}\")"
        }
    }

    fun generateLocalDate(): InstantiationFieldBuilder {
        return ConstantCallbackClassInstantiationField {
            val date = LocalDate.ofInstant(generateRandomInstant(), generateRandomZoneId())
            "java.time.LocalDate.parse(\"$date\")"
        }
    }

    fun generateLocalDateTime(): InstantiationFieldBuilder {
        return ConstantCallbackClassInstantiationField {
            val dateTime = LocalDateTime.ofInstant(generateRandomInstant(), generateRandomZoneId())
            "java.time.LocalDateTime.parse(\"$dateTime\")"
        }
    }

    fun generateLocalTime(): InstantiationFieldBuilder {
        return ConstantCallbackClassInstantiationField {
            val dateTime = LocalTime.ofInstant(generateRandomInstant(), generateRandomZoneId())
            "java.time.LocalTime.parse(\"$dateTime\")"
        }
    }

    fun generatePeriod(): InstantiationFieldBuilder {
        return ConstantCallbackClassInstantiationField {
            val first = LocalDate.ofInstant(generateRandomInstant(), generateRandomZoneId())
            val second = LocalDate.ofInstant(generateRandomInstant(), generateRandomZoneId())
            val period = Period.between(minOf(first, second), maxOf(first, second))
            "java.time.Period.parse(\"$period\")"
        }
    }

    private fun generateRandomZoneId(): ZoneId {
        val available = ZoneId.getAvailableZoneIds()
        return if (available.isEmpty()) ZoneId.systemDefault() else ZoneId.of(available.random())
    }

    private fun generateRandomInstant(): Instant {
        return Instant.ofEpochMilli(
            Random.nextLong(
                from = Instant.now().minus(365, ChronoUnit.DAYS).toEpochMilli(),
                until = Instant.now().toEpochMilli()
            )
        )
    }
}
