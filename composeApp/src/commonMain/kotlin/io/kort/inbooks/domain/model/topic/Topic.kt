package io.kort.inbooks.domain.model.topic

import io.kort.inbooks.domain.model.Editable
import kotlinx.datetime.Instant

data class Topic(
    val id: String,
    // Sorted by insertedAt.
    val books: List<TopicBook>,
    val title: String,
    val description: String,
    override val createdAt: Instant,
    override val modifiedAt: Instant?,
) : Editable