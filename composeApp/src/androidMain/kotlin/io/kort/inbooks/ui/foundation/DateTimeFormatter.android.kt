package io.kort.inbooks.ui.foundation

import android.text.format.DateFormat
import kotlinx.datetime.Instant
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

actual object DateTimeFormatter {
    actual fun formatByPattern(instant: Instant, pattern: String): String {
        val locale = Locale.getDefault()
        return SimpleDateFormat(DateFormat.getBestDateTimePattern(locale, pattern), locale)
            .format(Date(instant.toEpochMilliseconds()))
    }
}