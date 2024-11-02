package io.kort.inbooks.data.converter

import io.kort.inbooks.common.model.book.BookRemoteModel
import io.kort.inbooks.data.source.book.BookLocalModel
import io.kort.inbooks.data.source.collectedbook.CollectedBookLocalModel
import io.kort.inbooks.domain.model.book.Book
import io.kort.inbooks.domain.model.book.BookId
import io.kort.inbooks.domain.model.book.CollectedBook

fun BookLocalModel.toDomainModel(): Book {
    return Book(
        id = basic.id,
        source = basic.source.toDomainModel(),
        externalIds = externalIds.map { it.toDomainModel() },
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

fun BookLocalModel.BasicBookLocalModel.Source.toDomainModel() = when (this) {
    BookLocalModel.BasicBookLocalModel.Source.GoogleBookApi -> Book.Source.GoogleBookApi
    BookLocalModel.BasicBookLocalModel.Source.BooksUrl -> Book.Source.BooksUrl
}

fun BookLocalModel.ExternalIdLocalModel.toDomainModel(): Book.ExternalId {
    val domainType = when (type) {
        BookLocalModel.ExternalIdLocalModel.Type.ISBN13 -> Book.ExternalId.Type.ISBN13
        BookLocalModel.ExternalIdLocalModel.Type.GoogleBookId -> Book.ExternalId.Type.GoogleBookId
        BookLocalModel.ExternalIdLocalModel.Type.BooksUrl -> Book.ExternalId.Type.BooksUrl
    }

    return Book.ExternalId(
        type = domainType,
        value = value
    )
}

fun BookRemoteModel.toDomainModel(): Book {
    return Book(
        id = id,
        source = source.toDomainModel(),
        externalIds = externalIds.map { it.toDomainModel() },
        coverUrl = coverUrl,
        title = title,
        subtitle = subtitle,
        authors = authors,
        description = description,
        publishedDate = publishedDate,
        publisher = publisher,
        pageCount = pageCount,
        categories = categories,
    )
}

fun BookRemoteModel.Source.toDomainModel() = when (this) {
    BookRemoteModel.Source.GoogleBookApi -> Book.Source.GoogleBookApi
    BookRemoteModel.Source.BooksUrl -> Book.Source.BooksUrl
}

fun BookRemoteModel.ExternalIdRemoteModel.toDomainModel(): Book.ExternalId {
    return Book.ExternalId(
        type = type.toDomainModel(),
        value = value
    )
}

fun BookRemoteModel.ExternalIdRemoteModel.Type.toDomainModel(): Book.ExternalId.Type {
    return when (this) {
        BookRemoteModel.ExternalIdRemoteModel.Type.ISBN13 -> Book.ExternalId.Type.ISBN13
        BookRemoteModel.ExternalIdRemoteModel.Type.GoogleBookId -> Book.ExternalId.Type.GoogleBookId
        BookRemoteModel.ExternalIdRemoteModel.Type.BooksUrl -> Book.ExternalId.Type.BooksUrl
    }
}

fun Book.toLocalModel(): BookLocalModel {
    return BookLocalModel(
        basic = BookLocalModel.BasicBookLocalModel(
            id = id,
            source = source.toLocalModel(),
            coverUrl = coverUrl,
            title = title,
            subtitle = subtitle,
            description = description,
            publishedDate = publishedDate,
            publisher = publisher,
            pageCount = pageCount,
        ),
        externalIds = externalIds.map { it.toLocalModel(id) },
        authors = authors.orEmpty().map { BookLocalModel.AuthorLocalModel(name = it) },
        categories = categories.orEmpty().map { BookLocalModel.CategoryLocalModel(name = it) }
    )
}

fun Book.Source.toLocalModel() = when (this) {
    Book.Source.GoogleBookApi -> BookLocalModel.BasicBookLocalModel.Source.GoogleBookApi
    Book.Source.BooksUrl -> BookLocalModel.BasicBookLocalModel.Source.BooksUrl
}

fun Book.ExternalId.toLocalModel(bookId: BookId): BookLocalModel.ExternalIdLocalModel {
    return BookLocalModel.ExternalIdLocalModel(
        bookId = bookId,
        type = type.toLocalModel(),
        value = value
    )
}

fun Book.ExternalId.Type.toLocalModel(): BookLocalModel.ExternalIdLocalModel.Type {
    return when (this) {
        Book.ExternalId.Type.ISBN13 -> BookLocalModel.ExternalIdLocalModel.Type.ISBN13
        Book.ExternalId.Type.GoogleBookId -> BookLocalModel.ExternalIdLocalModel.Type.GoogleBookId
        Book.ExternalId.Type.BooksUrl -> BookLocalModel.ExternalIdLocalModel.Type.BooksUrl
    }
}

fun CollectedBook.toLocalModel(): CollectedBookLocalModel {
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

fun CollectedBook.ReadingEvent.toLocalModel(): CollectedBookLocalModel.ReadingEventLocalModel {
    return CollectedBookLocalModel.ReadingEventLocalModel(
        id = id,
        bookId = bookId,
        at = at,
        page = page,
        modifiedAt = modifiedAt,
        createdAt = createdAt,
    )
}

fun CollectedBookLocalModel.toDomainModel(): CollectedBook {
    return CollectedBook(
        book = book.toDomainModel(),
        readingEvent = readingEvents.map { it.toDomainModel() },
        readingReason = basic.readingReason,
        modifiedAt = basic.modifiedAt,
        createdAt = basic.createdAt
    )
}

fun CollectedBookLocalModel.ReadingEventLocalModel.toDomainModel(): CollectedBook.ReadingEvent {
    return CollectedBook.ReadingEvent(
        id = id,
        bookId = bookId,
        at = at,
        page = page,
        modifiedAt = modifiedAt,
        createdAt = createdAt,
    )
}