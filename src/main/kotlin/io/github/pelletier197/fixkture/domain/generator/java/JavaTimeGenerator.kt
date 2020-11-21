package io.github.pelletier197.fixkture.domain.generator.java

import io.github.pelletier197.fixkture.domain.InstantiationFieldBuilder
import io.github.pelletier197.fixkture.domain.generator.RegularCallbackClassInstantiationField
import java.time.*
import java.time.temporal.ChronoUnit
import kotlin.random.Random

object JavaTimeGenerator {
    fun generateInstant(): InstantiationFieldBuilder {
        return RegularCallbackClassInstantiationField {
            "java.time.Instant.parse(\"${generateRandomInstant()}\")"
        }
    }

    fun generateZoneDateTime(): InstantiationFieldBuilder {
        return RegularCallbackClassInstantiationField {
            "java.time.ZonedDateTime.parse(\"${generateRandomInstant().atZone(generateRandomZoneId())}\")"
        }
    }

    fun generateZoneId(): InstantiationFieldBuilder {
        return RegularCallbackClassInstantiationField {
            "java.time.ZoneId.of(\"${generateRandomZoneId().id}\")"
        }
    }

    fun generateLocalDate(): InstantiationFieldBuilder {
        return RegularCallbackClassInstantiationField {
            val date = LocalDate.ofInstant(generateRandomInstant(), generateRandomZoneId())
            "java.time.LocalDate.parse(\"$date\")"
        }
    }

    fun generateLocalDateTime(): InstantiationFieldBuilder {
        return RegularCallbackClassInstantiationField {
            val dateTime = LocalDateTime.ofInstant(generateRandomInstant(), generateRandomZoneId())
            "java.time.LocalDateTime.parse(\"$dateTime\")"
        }
    }

    fun generateLocalTime(): InstantiationFieldBuilder {
        return RegularCallbackClassInstantiationField {
            val dateTime = LocalTime.ofInstant(generateRandomInstant(), generateRandomZoneId())
            "java.time.LocalTime.parse(\"$dateTime\")"
        }
    }

    fun generatePeriod(): InstantiationFieldBuilder {
        return RegularCallbackClassInstantiationField {
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