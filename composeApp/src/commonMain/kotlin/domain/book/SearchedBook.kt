package domain.book

import kotlinx.serialization.Serializable

@Serializable
data class SearchedBook(
    val id: String,
    val coverUrl: String?,
    val name: String,
    val authors: List<String>,
    val description: String?,
)