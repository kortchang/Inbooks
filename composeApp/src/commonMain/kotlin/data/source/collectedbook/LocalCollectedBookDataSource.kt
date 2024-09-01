package data.source.collectedbook

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import data.source.book.BookLocalModel
import data.source.book.LocalBookDataSource
import data.source.collectedbook.model.CollectedBookLocalModel
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

@Dao
interface LocalCollectedBookDateSource : LocalBookDataSource, ForDashboard {
    @Transaction
    suspend fun insert(collectedBook: CollectedBookLocalModel) {
        val inserted = insert(collectedBook.book)
        if (inserted) {
            insertBasicCollectBook(collectedBook.basic)
            insertReadingEvent(collectedBook.readingEvents)
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBasicCollectBook(basicCollectedBook: CollectedBookLocalModel.BasicCollectedBookLocalModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReadingEvent(readingEvent: List<CollectedBookLocalModel.ReadingEventLocalModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReadingEvent(readingEvent: CollectedBookLocalModel.ReadingEventLocalModel)

    @Query("UPDATE book_reading_events SET at = :at WHERE id = :readingEventId")
    suspend fun updateReadingEventAt(readingEventId: String, at: Instant)

    @Query("SELECT * FROM collected_books WHERE book_id = :bookId")
    fun get(bookId: String): Flow<CollectedBookLocalModel?>

    @Query("SELECT * FROM book_authors_cross_reference")
    suspend fun getAllBookAuthorsCrossReference(): List<BookLocalModel.BookAuthorCrossReferenceLocalModel>

    @Query("UPDATE collected_books SET reading_reason = :reason WHERE book_id = :bookId")
    suspend fun updateReadingReason(bookId: String, reason: String)
}

interface ForDashboard {
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

    @Query(
        """
        SELECT collected_books.* FROM collected_books
        LEFT JOIN book_reading_events ON collected_books.book_id = book_reading_events.book_id
        WHERE book_reading_events.book_id IS NULL
        ORDER BY collected_books.created_at 
        DESC LIMIT 10
        """
    )
    fun getRecentCollectBooks(): Flow<List<CollectedBookLocalModel>>
}