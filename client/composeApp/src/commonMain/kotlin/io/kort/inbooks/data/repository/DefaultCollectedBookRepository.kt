package io.kort.inbooks.data.repository

import io.kort.inbooks.data.converter.toDomainModel
import io.kort.inbooks.data.converter.toLocalModel
import io.kort.inbooks.data.source.collectedbook.CollectedBookLocalDateSource
import io.kort.inbooks.domain.model.book.Book
import io.kort.inbooks.domain.model.book.BookId
import io.kort.inbooks.domain.model.book.CollectedBook
import io.kort.inbooks.domain.model.book.LocalYearAndMonth
import io.kort.inbooks.domain.repository.CollectedBookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.datetime.Clock

class DefaultCollectedBookRepository(
    private val local: CollectedBookLocalDateSource
) : CollectedBookRepository {
    override suspend fun insert(book: Book): String {
        return local.insert(book.toDefaultCollectedBook().toLocalModel())
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
}