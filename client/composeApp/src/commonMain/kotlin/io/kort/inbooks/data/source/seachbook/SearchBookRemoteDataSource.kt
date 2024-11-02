package io.kort.inbooks.data.source.seachbook

import io.kort.inbooks.common.model.book.BookRemoteModel
import io.kort.inbooks.domain.model.book.Book

interface SearchBookRemoteDataSource {
    fun isMatchedExternalType(type: Book.ExternalId.Type): Boolean
    suspend fun search(query: String): Result<List<BookRemoteModel>>
    suspend fun get(externalId: Book.ExternalId): Result<BookRemoteModel?>
}