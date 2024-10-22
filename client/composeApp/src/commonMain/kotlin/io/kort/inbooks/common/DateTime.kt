package io.kort.inbooks.common

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.asTimeZone
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.char
import kotlinx.datetime.format.format
import kotlinx.datetime.offsetIn
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(LocalDateTimeWithTimeZoneRFC9557Serializer::class)
data class LocalDateTimeWithTimeZone(
    val localDateTime: LocalDateTime,
    val timeZone: TimeZone = TimeZone.currentSystemDefault(),
) {
    private val utcOffset = localDateTime.toInstant(timeZone).offsetIn(timeZone)
    fun format(): String {
        return Formats.RFC_9557.format {
            setDateTime(localDateTime)
            setOffset(utcOffset)
            timeZoneId = timeZone.id
        }
    }

    object Formats {
        val RFC_9557: DateTimeFormat<DateTimeComponents>
            get() = DateTimeComponents.Format {
                dateTime(LocalDateTime.Formats.ISO)
                offset(UtcOffset.Formats.ISO)
                char('['); timeZoneId(); char(']')
            }
    }

    companion object {
        fun parseOrNull(value: String): LocalDateTimeWithTimeZone? {
            val components = Formats.RFC_9557.parseOrNull(value) ?: return null
            val timeZone = components.timeZoneId?.let { TimeZone.of(it) }
                ?: components.toUtcOffset().asTimeZone()

            return LocalDateTimeWithTimeZone(
                localDateTime = components.toLocalDateTime(),
                timeZone = timeZone
            )
        }

        fun parse(value: String): LocalDateTimeWithTimeZone {
            return parseOrNull(value)
                ?: throw IllegalArgumentException("Invalid LocalDateTimeWithTimeZone: $value")
        }
    }
}

class LocalDateTimeWithTimeZoneRFC9557Serializer : KSerializer<LocalDateTimeWithTimeZone> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("LocalDateTimeWithTimeZone", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LocalDateTimeWithTimeZone {
        return LocalDateTimeWithTimeZone.parse(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: LocalDateTimeWithTimeZone) {
        encoder.encodeString(value.format())
    }
}

fun Instant.isToday(): Boolean {
    val timeZone = TimeZone.currentSystemDefault()
    val today = Clock.System.now().toLocalDateTime(timeZone).date
    return today == this.toLocalDateTime(timeZone).date
}