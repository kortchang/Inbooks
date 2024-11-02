package io.kort.inbooks.domain.model.book

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias BookId = String

@Serializable
data class Book(
    val id: BookId,
    val externalIds: List<ExternalId>,
    val coverUrl: String?,
    val title: String,
    val subtitle: String?,
    val authors: List<String>?,
    val description: String?,
    val publishedDate: String?,
    val publisher: String?,
    val pageCount: Int?,
    val categories: List<String>?,
    val source: Source,
) {
    @Serializable
    data class ExternalId(
        val type: Type,
        val value: String,
    ) {
        @Serializable
        enum class Type {
            @SerialName("isbn_13")
            ISBN13,

            @SerialName("google_book_id")
            GoogleBookId,

            @SerialName("books_url")
            BooksUrl,
        }
    }

    @Serializable
    enum class Source {
        @SerialName("google_book_api")
        GoogleBookApi,

        @SerialName("books_url")
        BooksUrl
    }
}