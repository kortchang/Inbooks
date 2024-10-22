package io.kort.inbooks.data.converter

import io.kort.inbooks.data.source.book.BookLocalModel
import io.kort.inbooks.domain.model.book.Book

fun BookLocalModel.toDomainModel(): Book {
    return Book(
        id = basic.id,
        externalIds = externalIds.toDomainModels(),
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

private fun List<BookLocalModel.ExternalIdLocalModel>.toDomainModels(): List<Book.ExternalId> {
    return map { localExternalId ->
        val domainType = when (localExternalId.type) {
            BookLocalModel.ExternalIdLocalModel.Type.ISBN13 -> Book.ExternalId.Type.ISBN13
            BookLocalModel.ExternalIdLocalModel.Type.GoogleBookId -> Book.ExternalId.Type.GoogleBookId
        }
        Book.ExternalId(
            type = domainType,
            value = localExternalId.value
        )
    }
}