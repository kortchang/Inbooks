package domain.book

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

/**
 * 從平台直接拿下來的書籍資訊
 */
@Serializable
data class SearchedBook(
    val book: Book,
    val averageRating: Double?,
    val ratingsCount: Int?,
    val searchTextSnippet: String?,
)