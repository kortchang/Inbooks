package io.kort.inbooks.data.repository

import io.kort.inbooks.common.service.book.BookUrlAnalyzer
import io.kort.inbooks.data.converter.toDomainModel
import io.kort.inbooks.domain.model.book.Book
import io.kort.inbooks.domain.repository.GetBookByUrlRepository

class DefaultGetBookByUrlRepository(
    private val bookUrlAnalyzer: BookUrlAnalyzer,
) : GetBookByUrlRepository {
    override suspend fun get(url: String): Result<Book> {
        val bookOrNull = bookUrlAnalyzer.analyze(url)?.toDomainModel()
        return if (bookOrNull != null) {
            Result.success(bookOrNull)
        } else {
            Result.failure(IllegalArgumentException("Invalid book url"))
        }
    }
}