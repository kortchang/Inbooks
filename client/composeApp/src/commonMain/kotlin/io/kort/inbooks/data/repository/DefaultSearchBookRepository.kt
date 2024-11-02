package io.kort.inbooks.data.repository

import io.kort.inbooks.data.source.book.BookLocalDataSource
import io.kort.inbooks.common.model.book.BookRemoteModel
import io.kort.inbooks.data.converter.toDomainModel
import io.kort.inbooks.data.converter.toLocalModel
import io.kort.inbooks.data.source.seachbook.SearchBookRemoteDataSource
import io.kort.inbooks.domain.model.book.Book
import io.kort.inbooks.domain.model.book.SearchedBook
import io.kort.inbooks.domain.repository.SearchBookRepository

class DefaultSearchBookRepository(
    private val local: BookLocalDataSource,
    private val remote: SearchBookRemoteDataSource,
) : SearchBookRepository {
    override suspend fun search(query: String): Result<List<SearchedBook>> {
        return remote.search(query).map { it.map { SearchedBook(it.toDomainModel()) } }
    }

    override suspend fun get(externalIds: List<Book.ExternalId>): Result<SearchedBook?> {
        return runCatching {
            if (externalIds.isEmpty()) {
                return@runCatching null
            }

            // find local data source
            val localBook = externalIds.firstNotNullOfOrNull { externalId ->
                val bookIdOrNull = local.getBookIdByExternalId(externalId.type.toLocalModel() to externalId.value)
                bookIdOrNull?.let { local.getById(it) }
            }

            if (localBook != null) {
                return@runCatching SearchedBook(localBook.toDomainModel())
            }

            var latestThrowable: Throwable? = null
            externalIds.firstNotNullOfOrNull { externalId ->
                remote.get(externalId)
                    .onSuccess {
                        return@runCatching it?.let { SearchedBook(it.toDomainModel()) }
                    }
                    .onFailure {
                        latestThrowable = it
                    }
            }

            if (latestThrowable != null) {
                throw latestThrowable!!
            } else {
                null
            }
        }
    }
}