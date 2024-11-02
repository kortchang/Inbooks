package io.kort.inbooks.common.model.book

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookRemoteModel(
    val id: String,
    val externalIds: List<ExternalIdRemoteModel>,
    val source: Source,
    val coverUrl: String?,
    val title: String,
    val subtitle: String?,
    val authors: List<String>?,
    val description: String?,
    val publishedDate: String?,
    val publisher: String?,
    val pageCount: Int?,
    val categories: List<String>?,
) {
    @Serializable
    data class ExternalIdRemoteModel(
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