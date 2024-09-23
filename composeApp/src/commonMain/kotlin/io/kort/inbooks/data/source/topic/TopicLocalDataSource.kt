package io.kort.inbooks.data.source.topic

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import io.kort.inbooks.domain.model.book.BookId
import io.kort.inbooks.domain.model.book.LocalYearAndMonth
import kotlinx.coroutines.flow.Flow

@Dao
interface TopicLocalDataSource {
    @Transaction
    suspend fun insert(
        topic: BasicTopicLocalModel,
        references: List<BasicTopicLocalModel.TopicBookCrossReferenceLocalModel>
    ) {
        insertBasicTopic(topic)
        insertTopicBookCrossReference(references)
    }

    @Insert
    suspend fun insertBasicTopic(topic: BasicTopicLocalModel)

    @Insert
    suspend fun insertTopicBookCrossReference(references: List<BasicTopicLocalModel.TopicBookCrossReferenceLocalModel>)

    @Transaction
    suspend fun update(
        topic: BasicTopicLocalModel,
        references: List<BasicTopicLocalModel.TopicBookCrossReferenceLocalModel>
    ) {
        updateBasicTopic(topic)
        updateWholeTopicBookCrossReference(topicId = topic.id, references)
    }

    @Update
    suspend fun updateBasicTopic(topic: BasicTopicLocalModel)

    suspend fun updateWholeTopicBookCrossReference(
        topicId: String,
        references: List<BasicTopicLocalModel.TopicBookCrossReferenceLocalModel>
    ) {
        /**
         * 新增或更新清單的書籍
         */
        upsertTopicBookCrossReference(references)

        /**
         * 移除已經不再清單內的書籍
         */
        deleteNotInListTopicBookCrossReference(topicId, references.map { it.bookId })
    }

    @Upsert
    suspend fun upsertTopicBookCrossReference(
        references: List<BasicTopicLocalModel.TopicBookCrossReferenceLocalModel>
    )

    @Query(
        """ 
            DELETE FROM topic_book_cross_reference
            WHERE topic_id = :topicId AND book_id NOT IN (:bookIds)
        """
    )
    suspend fun deleteNotInListTopicBookCrossReference(
        topicId: String,
        bookIds: List<BookId>
    )

    @Transaction
    @Query("DELETE FROM topics WHERE id = :id")
    suspend fun delete(id: String)

    @Transaction
    @Query("SELECT * FROM topics ORDER BY created_at DESC")
    fun getAll(): Flow<List<TopicLocalModel>>

    @Transaction
    @Query("SELECT * FROM topics WHERE id = :id")
    fun get(id: String): Flow<TopicLocalModel?>

    @Transaction
    @Query(
        """
            SELECT strftime('%Y-%m', topics.created_at, 'unixepoch') AS year_month, topics.*
            FROM topics
            ORDER BY topics.created_at DESC
        """
    )
    fun getCreatedAtLocalYearAndMonthToTopics(): Flow<Map<
            @MapColumn("year_month") LocalYearAndMonth, List<TopicLocalModel>>
            >

    @Transaction
    @Query("SELECT * FROM topics ORDER BY created_at DESC LIMIT 5")
    fun getRecent(): Flow<List<TopicLocalModel>>
}