package io.kort.inbooks.domain.model

import kotlinx.datetime.Instant

interface Editable {
    val modifiedAt: Instant?
    val createdAt: Instant
}