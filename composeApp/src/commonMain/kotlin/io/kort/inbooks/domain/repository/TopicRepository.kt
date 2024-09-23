package io.kort.inbooks.domain.repository

import io.kort.inbooks.domain.model.book.LocalYearAndMonth
import io.kort.inbooks.domain.model.topic.Topic
import kotlinx.coroutines.flow.Flow

interface TopicRepository {
    suspend fun insert(topic: Topic)
    fun getAll(): Flow<List<Topic>>
    fun get(id: String): Flow<Topic?>
    fun getRecent(): Flow<List<Topic>>
    fun getCreatedAtLocalYearAndMonthToTopics(): Flow<Map<LocalYearAndMonth, List<Topic>>>
    suspend fun update(topic: Topic)
    suspend fun delete(id: String)
}