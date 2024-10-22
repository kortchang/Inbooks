package io.kort.inbooks.data.repository

import io.kort.inbooks.data.converter.toDomainModel
import io.kort.inbooks.data.source.topic.BasicTopicLocalModel
import io.kort.inbooks.data.source.topic.TopicLocalDataSource
import io.kort.inbooks.data.source.topic.TopicLocalModel
import io.kort.inbooks.domain.model.book.LocalYearAndMonth
import io.kort.inbooks.domain.model.topic.Topic
import io.kort.inbooks.domain.model.topic.TopicBook
import io.kort.inbooks.domain.repository.TopicRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

class DefaultTopicRepository(private val local: TopicLocalDataSource) : TopicRepository {
    override suspend fun insert(topic: Topic) {
        local.insert(
            topic = topic.toBasicLocalModel(),
            references = topic.books.map { it.toDomainModel(topicId = topic.id) }
        )
    }

    override fun getAll(): Flow<List<Topic>> {
        return local.getAll().mapLatest { list ->
            list.map { it.toDomainModel() }
        }
    }

    override fun get(id: String): Flow<Topic?> {
        return local.get(id).mapLatest { it?.toDomainModel() }
    }

    override fun getRecent(): Flow<List<Topic>> {
        return local.getRecent().mapLatest { list ->
            list.map { it.toDomainModel() }
        }
    }

    override fun getCreatedAtLocalYearAndMonthToTopics(): Flow<Map<LocalYearAndMonth, List<Topic>>> {
        return local.getCreatedAtLocalYearAndMonthToTopics().mapLatest { map ->
            map.mapValues { (_, list) -> list.map { it.toDomainModel() } }
        }
    }

    override suspend fun update(topic: Topic) {
        local.update(
            topic = topic.toBasicLocalModel(),
            references = topic.books.map { it.toDomainModel(topicId = topic.id) }
        )
    }

    override suspend fun delete(id: String) {
        local.delete(id)
    }

    private fun Topic.toBasicLocalModel(): BasicTopicLocalModel = BasicTopicLocalModel(
        id = id,
        title = title,
        description = description,
        createdAt = createdAt,
        modifiedAt = modifiedAt,
    )

    private fun TopicLocalModel.toDomainModel(): Topic = Topic(
        id = topic.id,
        books = books.toDomainModel(),
        title = topic.title,
        description = topic.description,
        createdAt = topic.createdAt,
        modifiedAt = topic.modifiedAt,
    )

    private fun TopicBook.toDomainModel(
        topicId: String
    ): BasicTopicLocalModel.TopicBookCrossReferenceLocalModel {
        return BasicTopicLocalModel.TopicBookCrossReferenceLocalModel(
            topicId = topicId,
            bookId = book.id,
            createdAt = insertedAt,
        )
    }

    private fun List<BasicTopicLocalModel.TopicBookLocalModel>.toDomainModel(): List<TopicBook> {
        return map { it.toDomainModel() }.sortedBy { it.insertedAt }
    }

    private fun BasicTopicLocalModel.TopicBookLocalModel.toDomainModel(): TopicBook {
        return TopicBook(
            book = book.toDomainModel(),
            insertedAt = reference.createdAt,
        )
    }
}