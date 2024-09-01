package data.repository

import data.source.book.BookLocalModel
import data.source.collectedbook.LocalCollectedBookDateSource
import data.source.collectedbook.model.CollectedBookLocalModel
import domain.book.Book
import domain.book.CollectedBook
import domain.book.SearchedBook
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock

interface CollectedBookRepository {
    suspend fun insert(searchedBook: SearchedBook): String
    suspend fun insertReadingEvent(readingEvent: CollectedBook.ReadingEvent)

    fun getRecentReadingBooks(): Flow<List<CollectedBook>>
    fun getRecentReadBooks(): Flow<List<CollectedBook>>
    fun getRecentCollectBooks(): Flow<List<CollectedBook>>
    fun get(bookId: String): Flow<CollectedBook?>

    suspend fun updateReadingReason(bookId: String, reason: String)
}

class DefaultCollectedBookRepository(
    private val local: LocalCollectedBookDateSource
) : CollectedBookRepository {
    override suspend fun insert(searchedBook: SearchedBook): String {
        val book = searchedBook.toDefaultCollectedBook().toLocalModel()
        local.insert(book)
        return book.basic.bookId
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

    override suspend fun insertReadingEvent(readingEvent: CollectedBook.ReadingEvent) {
        local.insertReadingEvent(readingEvent.toLocalModel())
    }

    override fun getRecentCollectBooks(): Flow<List<CollectedBook>> {
        return local.getRecentCollectBooks().map { list ->
            list.map { it.toDomainModel() }
        }
    }

    override fun get(bookId: String): Flow<CollectedBook?> {
        return local.get(bookId).map { it?.toDomainModel() }
    }

    override suspend fun updateReadingReason(bookId: String, reason: String) {
        local.updateReadingReason(bookId, reason)
    }

    private fun SearchedBook.toDefaultCollectedBook(): CollectedBook {
        return CollectedBook(
            book = book,
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
                idBySource = idBySource,
                source = source,
                isbn13 = isbn13,
                coverUrl = coverUrl,
                title = title,
                subtitle = subtitle,
                description = description,
                publishedDate = publishedDate,
                publisher = publisher,
                pageCount = pageCount,
            ),
            authors = authors.orEmpty().map { BookLocalModel.AuthorLocalModel(name = it) },
            categories = categories.orEmpty().map { BookLocalModel.CategoryLocalModel(name = it) }
        )
    }

    private fun BookLocalModel.toDomainModel(): Book {
        return Book(
            id = basic.id,
            source = basic.source,
            idBySource = basic.idBySource,
            isbn13 = basic.isbn13,
            coverUrl = basic.coverUrl,
            title = basic.title,
            subtitle = basic.subtitle,
            authors = authors.map { it.name },
            description = basic.description,
            publishedDate = basic.publishedDate,
            publisher = basic.publisher,
            pageCount = basic.pageCount,
            categories = categories.map { it.name },
        )
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