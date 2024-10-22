package io.kort.inbooks.ui.foundation

import kotlinx.datetime.Instant

expect object DateTimeFormatter {
    fun formatByPattern(instant: Instant, pattern: String): String
}