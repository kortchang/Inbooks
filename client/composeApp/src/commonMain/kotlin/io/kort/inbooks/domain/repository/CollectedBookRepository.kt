package io.kort.inbooks.domain.repository

import io.kort.inbooks.domain.model.book.Book
import io.kort.inbooks.domain.model.book.BookId
import io.kort.inbooks.domain.model.book.CollectedBook
import io.kort.inbooks.domain.model.book.LocalYearAndMonth
import kotlinx.coroutines.flow.Flow

interface CollectedBookRepository {
    suspend fun insert(book: Book): String
    suspend fun insertReadingEvent(readingEvent: CollectedBook.ReadingEvent)

    fun getRecentReadingBooks(): Flow<List<CollectedBook>>
    fun getRecentReadBooks(): Flow<List<CollectedBook>>
    fun getRecentCollectBooks(): Flow<List<CollectedBook>>
    fun getAll(): Flow<List<CollectedBook>>
    fun get(bookId: String): Flow<CollectedBook?>
    suspend fun getBookIdToCollectedBook(bookIds: List<BookId>): Map<BookId, CollectedBook>
    fun getBookIdToCollectedBookFlow(bookIds: List<BookId>): Flow<Map<BookId, CollectedBook>>
    fun getCreatedAtLocalYearAndMonthToCollectionBooks(): Flow<Map<LocalYearAndMonth, List<CollectedBook>>>

    suspend fun updateReadingReason(bookId: String, reason: String)

    suspend fun delete(bookId: String)
}