package io.kort.inbooks.domain.model.book

import com.benasher44.uuid.uuid4
import io.kort.inbooks.domain.model.Editable
import kotlinx.datetime.Instant

data class CollectedBook(
    val book: Book,
    val readingEvent: List<ReadingEvent>,
    val readingReason: String,
    override val modifiedAt: Instant?,
    override val createdAt: Instant,
) : Editable {
    data class ReadingEvent(
        val id: String = uuid4().toString(),
        val bookId: String,
        val at: Instant,
        val page: Int,
        override val modifiedAt: Instant?,
        override val createdAt: Instant,
    ) : Editable
}