package io.kort.inbooks.domain.model.book

import kotlinx.serialization.Serializable

/**
 * 從平台直接拿下來的書籍資訊
 */
@Serializable
data class SearchedBook(
    val book: Book,
)