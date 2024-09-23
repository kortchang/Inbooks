package io.kort.inbooks.data.source.collectedbook

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import io.kort.inbooks.data.source.book.BookLocalModel
import io.kort.inbooks.data.source.book.BookLocalDataSource
import io.kort.inbooks.domain.model.book.BookId
import io.kort.inbooks.domain.model.book.LocalYearAndMonth
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

@Dao
interface CollectedBookLocalDateSource : BookLocalDataSource, ForDashboard {
    @Transaction
    suspend fun insert(collectedBook: CollectedBookLocalModel) {
        val existOrNewBookId = insert(collectedBook.book)
        insertOrIgnoreBasicCollectBook(collectedBook.basic.copy(bookId = existOrNewBookId))
        insertOrIgnoreReadingEvent(collectedBook.readingEvents.map { it.copy(bookId = existOrNewBookId) })
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreBasicCollectBook(basicCollectedBook: CollectedBookLocalModel.BasicCollectedBookLocalModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreReadingEvent(readingEvent: List<CollectedBookLocalModel.ReadingEventLocalModel>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreReadingEvent(readingEvent: CollectedBookLocalModel.ReadingEventLocalModel)

    @Query("UPDATE book_reading_events SET at = :at WHERE id = :readingEventId")
    suspend fun updateReadingEventAt(readingEventId: String, at: Instant)

    @Transaction
    @Query("SELECT * FROM collected_books WHERE book_id = :bookId")
    fun get(bookId: String): Flow<CollectedBookLocalModel?>

    @Transaction
    @Query("SELECT * FROM collected_books ORDER BY created_at DESC")
    fun getAll(): Flow<List<CollectedBookLocalModel>>

    @Query("SELECT * FROM book_authors_cross_reference")
    suspend fun getAllBookAuthorsCrossReference(): List<BookLocalModel.BookAuthorCrossReferenceLocalModel>

    @Transaction
    suspend fun getCollectedBookByExternalIds(
        externalIds: List<Pair<BookLocalModel.ExternalIdLocalModel.Type, String>>
    ): CollectedBookLocalModel? {
        return externalIds.firstNotNullOfOrNull { getCollectedBookByExternalId(it.first, it.second) }
    }

    @Query(
        """
            SELECT collected_books.* 
            FROM (
                SELECT external_ids.book_id
                FROM book_external_ids external_ids
                WHERE external_ids.type = :type AND external_ids.value = :value
            ) AS matched_external_id
            JOIN collected_books ON collected_books.book_id = matched_external_id.book_id
        """
    )
    suspend fun getCollectedBookByExternalId(
        type: BookLocalModel.ExternalIdLocalModel.Type,
        value: String
    ): CollectedBookLocalModel?

    @Query(
        """
        SELECT * FROM collected_books 
        WHERE book_id IN (:bookIds)
        GROUP BY book_id
        """
    )
    suspend fun getBookIdToCollectedBook(bookIds: List<BookId>): Map<
        @MapColumn(columnName = "book_id", tableName = "collected_books")
        BookId,
        CollectedBookLocalModel
        >

    @Query(
        """
        SELECT * FROM collected_books 
        WHERE book_id IN (:bookIds)
        GROUP BY book_id
        """
    )
    fun getBookIdToCollectedBookFlow(bookIds: List<BookId>): Flow<
        Map<
            @MapColumn(columnName = "book_id", tableName = "collected_books")
            BookId,
            CollectedBookLocalModel
            >
        >

    @Transaction
    @Query(
        """
            SELECT strftime('%Y-%m', collected_books.created_at, 'unixepoch') AS year_month, collected_books.*
            FROM collected_books
            ORDER BY collected_books.created_at DESC
        """
    )
    fun getCreatedAtLocalYearAndMonthToCollectedBook(): Flow<
        Map<
            @MapColumn("year_month")
            LocalYearAndMonth,
            List<CollectedBookLocalModel>
            >
        >

    @Query("UPDATE collected_books SET reading_reason = :reason WHERE book_id = :bookId")
    suspend fun updateReadingReason(bookId: String, reason: String)

    @Query("DELETE FROM collected_books WHERE book_id = :bookId")
    suspend fun delete(bookId: String)
}

interface ForDashboard {
    @Transaction
    @Query(
        """
        SELECT collected_books.*
        FROM (
            SELECT event.book_id, MAX(event.page) AS latest_page, event.at
            FROM book_reading_events event
            GROUP BY event.book_id
        ) AS latest_page_reading_event
        JOIN books ON books.id = latest_page_reading_event.book_id
        AND latest_page_reading_event.latest_page != 0
        AND books.page_count != latest_page_reading_event.latest_page
        JOIN collected_books ON collected_books.book_id = latest_page_reading_event.book_id
        ORDER BY latest_page_reading_event.at DESC;
        """
    )
    fun getRecentReadingBooks(): Flow<List<CollectedBookLocalModel>>

    @Transaction
    @Query(
        """
        SELECT collected_books.*
        FROM (
            SELECT event.book_id, MAX(event.page) AS latest_page, event.at
            FROM book_reading_events event
            GROUP BY event.book_id
        ) AS latest_page_reading_event
        JOIN books ON books.id = latest_page_reading_event.book_id
        AND books.page_count = latest_page_reading_event.latest_page
        JOIN collected_books ON collected_books.book_id = latest_page_reading_event.book_id
        ORDER BY latest_page_reading_event.at DESC;
        """
    )
    fun getRecentReadBooks(): Flow<List<CollectedBookLocalModel>>

    @Transaction
    @Query(
        """
        SELECT * FROM collected_books
        ORDER BY created_at
        DESC LIMIT 5
        """
    )
    fun getRecentCollectBooks(): Flow<List<CollectedBookLocalModel>>
}
