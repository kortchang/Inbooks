package io.kort.inbooks.data.source.seachbook

import io.kort.inbooks.common.model.book.BookRemoteModel
import io.kort.inbooks.data.converter.toDomainModel
import io.kort.inbooks.data.converter.toLocalModel
import io.kort.inbooks.data.source.book.BookLocalDataSource
import io.kort.inbooks.data.source.seachbook.google.BookGoogleRemoteDataSource
import io.kort.inbooks.domain.model.book.Book

class SearchBookCombinedRemoteDataSource(
    private val localBooksDataSource: BookLocalDataSource,
    googleBooksDataSource: BookGoogleRemoteDataSource,
) : SearchBookRemoteDataSource {
    private val dataSources: List<SearchBookRemoteDataSource> = listOf(googleBooksDataSource)

    override fun isMatchedExternalType(type: Book.ExternalId.Type): Boolean {
        return dataSources.any { it.isMatchedExternalType(type) }
    }

    override suspend fun search(query: String): Result<List<BookRemoteModel>> {
        return runCatching {
            dataSources.fold(emptyList()) { acc, dataSource ->
                acc + dataSource.search(query)
                    .getOrThrow()
                    .map { it.replaceBookIfByLocalIfNeeded() }
            }
        }
    }

    override suspend fun get(externalId: Book.ExternalId): Result<BookRemoteModel?> {
        return runCatching {
            dataSources.firstNotNullOfOrNull { dataSource ->
                dataSource.get(externalId)
                    .getOrThrow()
                    ?.replaceBookIfByLocalIfNeeded()
            }
        }
    }

    private suspend fun BookRemoteModel.replaceBookIfByLocalIfNeeded(): BookRemoteModel {
        val existBookId = localBooksDataSource
            .getBookIdByExternalIds(
                externalIds = externalIds.map { it.type.toDomainModel().toLocalModel() to it.value }
            )
        return if (existBookId != null) copy(id = existBookId) else this
    }
}