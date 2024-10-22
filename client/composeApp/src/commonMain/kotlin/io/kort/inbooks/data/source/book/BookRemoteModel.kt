package io.kort.inbooks.data.source.book

data class BookRemoteModel(
    val id: String,
    val externalIds: List<ExternalId>,
    val coverUrl: String? = null,
    val title: String,
    val subtitle: String? = null,
    val description: String? = null,
    val authors: List<String>? = null,
    val publishedDate: String? = null,
    val publisher: String? = null,
    val pageCount: Int? = null,
    val categories: List<String>? = null,
) {
    data class ExternalId(
        val type: Type,
        val value: String,
    ) {
        enum class Type {
            ISBN13,
            GoogleBookId
        }
    }
}