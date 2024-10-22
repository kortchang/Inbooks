package io.kort.inbooks.data.repository

import io.kort.inbooks.data.source.book.BookRemoteModel
import io.kort.inbooks.data.source.seachbook.SearchBookRemoteDataSource
import io.kort.inbooks.domain.model.book.Book
import io.kort.inbooks.domain.model.book.SearchedBook
import io.kort.inbooks.domain.repository.SearchBookRepository

class DefaultSearchBookRepository(
    private val remote: SearchBookRemoteDataSource,
) : SearchBookRepository {
    private fun BookRemoteModel.toDomain(): Book {
        return Book(
            id = id,
            externalIds = externalIds.map { it.toDomain() },
            coverUrl = coverUrl,
            title = title,
            subtitle = subtitle,
            authors = authors ?: emptyList(),
            description = description,
            publishedDate = publishedDate,
            publisher = publisher,
            pageCount = pageCount,
            categories = categories,
        )
    }

    private fun BookRemoteModel.ExternalId.toDomain(): Book.ExternalId {
        val type = when (type) {
            BookRemoteModel.ExternalId.Type.ISBN13 -> Book.ExternalId.Type.ISBN13
            BookRemoteModel.ExternalId.Type.GoogleBookId -> Book.ExternalId.Type.GoogleBookId
        }
        return Book.ExternalId(type, value)
    }

    override suspend fun search(query: String): Result<List<SearchedBook>> {
        return remote.search(query).map { it.map { SearchedBook(it.toDomain()) } }
    }

    override suspend fun get(externalIds: List<Book.ExternalId>): Result<SearchedBook?> {
        return runCatching {
            var latestThrowable: Throwable? = null
            val finalResult = externalIds.firstNotNullOfOrNull { externalId ->
                val result = remote.get(externalId)

                if (result.isSuccess) {
                    result
                        .getOrNull()
                        ?.toDomain()
                        ?.let { SearchedBook(it) }
                } else {
                    latestThrowable = result.exceptionOrNull()
                    null
                }
            }

            if (finalResult == null && latestThrowable != null) {
                throw latestThrowable!!
            } else {
                finalResult
            }
        }
    }
}