package io.kort.inbooks.domain.repository

import io.kort.inbooks.domain.model.book.Book
import io.kort.inbooks.domain.model.book.SearchedBook

interface SearchBookRepository {
    suspend fun search(query: String): Result<List<SearchedBook>>
    suspend fun get(externalIds: List<Book.ExternalId>): Result<SearchedBook?>
}

sealed class SearchBookError : Error() {
    abstract val errorCode: Int
    abstract val errorMessage: String
    override val message: String?
        get() = "$errorCode: $errorMessage"

    data class TooManyRequests(override val errorCode: Int, override val errorMessage: String) : SearchBookError()
}