package io.kort.inbooks.domain.repository

import io.kort.inbooks.domain.model.book.Book

interface GetBookByUrlRepository {
    suspend fun get(url: String): Result<Book>
}