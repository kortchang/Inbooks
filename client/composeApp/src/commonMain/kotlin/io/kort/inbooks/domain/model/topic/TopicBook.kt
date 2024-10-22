package io.kort.inbooks.domain.model.topic

import io.kort.inbooks.domain.model.book.Book
import kotlinx.datetime.Instant

data class TopicBook(
    val book: Book,
    val insertedAt: Instant,
)