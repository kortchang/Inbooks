package io.kort.inbooks.data.repository

import io.kort.inbooks.data.converter.toDomainModel
import io.kort.inbooks.data.source.book.BookLocalModel
import io.kort.inbooks.data.source.collectedbook.CollectedBookLocalDateSource
import io.kort.inbooks.data.source.collectedbook.CollectedBookLocalModel
import io.kort.inbooks.domain.model.book.Book
import io.kort.inbooks.domain.model.book.BookId
import io.kort.inbooks.domain.model.book.CollectedBook
import io.kort.inbooks.domain.model.book.LocalYearAndMonth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.datetime.Clock

class DefaultCollectedBookRepository(
    private val local: CollectedBookLocalDateSource
) : CollectedBookRepository {
    override suspend fun insert(book: Book): String {
        local.insert(book.toDefaultCollectedBook().toLocalModel())
        return book.id
    }

    override suspend fun insertReadingEvent(readingEvent: CollectedBook.ReadingEvent) {
        local.insertOrIgnoreReadingEvent(readingEvent.toLocalModel())
    }

    override fun getRecentReadingBooks(): Flow<List<CollectedBook>> {
        return local.getRecentReadingBooks().map { list ->
            list.map { it.toDomainModel() }
        }
    }

    override fun getRecentReadBooks(): Flow<List<CollectedBook>> {
        return local.getRecentReadBooks().map { list ->
            list.map { it.toDomainModel() }
        }
    }

    override fun getRecentCollectBooks(): Flow<List<CollectedBook>> {
        return local.getRecentCollectBooks().map { list ->
            list.map { it.toDomainModel() }
        }
    }

    override fun getAll(): Flow<List<CollectedBook>> {
        return local.getAll().mapLatest { list ->
            list.map { it.toDomainModel() }
        }
    }

    override fun get(bookId: String): Flow<CollectedBook?> {
        return local.get(bookId).map { it?.toDomainModel() }
    }

    override suspend fun getBookIdToCollectedBook(bookIds: List<BookId>): Map<BookId, CollectedBook> {
        return local.getBookIdToCollectedBook(bookIds).mapValues { it.value.toDomainModel() }
    }

    override fun getBookIdToCollectedBookFlow(bookIds: List<BookId>): Flow<Map<BookId, CollectedBook>> {
        return local.getBookIdToCollectedBookFlow(bookIds).map { map ->
            map.mapValues { it.value.toDomainModel() }
        }
    }

    override fun getCreatedAtLocalYearAndMonthToCollectionBooks(): Flow<Map<LocalYearAndMonth, List<CollectedBook>>> {
        return local.getCreatedAtLocalYearAndMonthToCollectedBook().map { map ->
            map.mapValues { it.value.map { it.toDomainModel() } }
        }
    }

    override suspend fun updateReadingReason(bookId: String, reason: String) {
        local.updateReadingReason(bookId, reason)
    }

    override suspend fun delete(bookId: String) {
        local.delete(bookId)
    }

    private fun Book.toDefaultCollectedBook(): CollectedBook {
        return CollectedBook(
            book = this,
            readingEvent = emptyList(),
            readingReason = "",
            modifiedAt = null,
            createdAt = Clock.System.now()
        )
    }

    private fun Book.toLocalModel(): BookLocalModel {
        return BookLocalModel(
            basic = BookLocalModel.BasicBookLocalModel(
                id = id,
                coverUrl = coverUrl,
                title = title,
                subtitle = subtitle,
                description = description,
                publishedDate = publishedDate,
                publisher = publisher,
                pageCount = pageCount,
            ),
            externalIds = externalIds.toLocalModels(id),
            authors = authors.orEmpty().map { BookLocalModel.AuthorLocalModel(name = it) },
            categories = categories.orEmpty().map { BookLocalModel.CategoryLocalModel(name = it) }
        )
    }

    private fun List<Book.ExternalId>.toLocalModels(bookId: BookId): List<BookLocalModel.ExternalIdLocalModel> {
        return map { domainExternalId ->
            BookLocalModel.ExternalIdLocalModel(
                bookId = bookId,
                type = domainExternalId.type.toLocalModel(),
                value = domainExternalId.value
            )
        }
    }

    private fun Book.ExternalId.Type.toLocalModel(): BookLocalModel.ExternalIdLocalModel.Type {
        return when (this) {
            Book.ExternalId.Type.ISBN13 -> BookLocalModel.ExternalIdLocalModel.Type.ISBN13
            Book.ExternalId.Type.GoogleBookId -> BookLocalModel.ExternalIdLocalModel.Type.GoogleBookId
        }
    }

    private fun CollectedBook.toLocalModel(): CollectedBookLocalModel {
        return CollectedBookLocalModel(
            basic = CollectedBookLocalModel.BasicCollectedBookLocalModel(
                bookId = book.id,
                readingReason = readingReason,
                modifiedAt = modifiedAt,
                createdAt = createdAt
            ),
            book = book.toLocalModel(),
            readingEvents = readingEvent.map { it.toLocalModel() }
        )
    }

    private fun CollectedBook.ReadingEvent.toLocalModel(): CollectedBookLocalModel.ReadingEventLocalModel {
        return CollectedBookLocalModel.ReadingEventLocalModel(
            id = id,
            bookId = bookId,
            at = at,
            page = page,
            modifiedAt = modifiedAt,
            createdAt = createdAt,
        )
    }

    private fun CollectedBookLocalModel.toDomainModel(): CollectedBook {
        return CollectedBook(
            book = book.toDomainModel(),
            readingEvent = readingEvents.map { it.toDomainModel() },
            readingReason = basic.readingReason,
            modifiedAt = basic.modifiedAt,
            createdAt = basic.createdAt
        )
    }

    private fun CollectedBookLocalModel.ReadingEventLocalModel.toDomainModel(): CollectedBook.ReadingEvent {
        return CollectedBook.ReadingEvent(
            id = id,
            bookId = bookId,
            at = at,
            page = page,
            modifiedAt = modifiedAt,
            createdAt = createdAt,
        )
    }
}