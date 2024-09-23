package io.kort.inbooks.ui.foundation

import kotlinx.datetime.Instant
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSTimeZone
import platform.Foundation.localTimeZone

actual object DateTimeFormatter {
    actual fun formatByPattern(instant: Instant, pattern: String): String {
        val formatter = NSDateFormatter().apply {
            dateFormat = pattern
            timeZone = NSTimeZone.localTimeZone
        }
        val date = NSDate(instant.epochSeconds.toDouble())
        return formatter.stringFromDate(date)
    }
}