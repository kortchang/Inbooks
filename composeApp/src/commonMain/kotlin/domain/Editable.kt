package domain

import kotlinx.datetime.Instant

interface Editable {
    val modifiedAt: Instant?
    val createdAt: Instant
}