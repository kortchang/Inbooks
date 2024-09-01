package data.source.seachbook

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchedBooksRemoteModel(
    @SerialName("items")
    val items: List<SearchedBookRemoteModel>?,
    @SerialName("totalItems")
    val totalItems: Int
) {
    @Serializable
    data class SearchedBookRemoteModel(
        @SerialName("id")
        val id: String,
        @SerialName("volumeInfo")
        val volumeInfo: VolumeInfoRemoteModel
    ) {
        @Serializable
        data class VolumeInfoRemoteModel(
            @SerialName("title")
            val title: String,
            @SerialName("subtitle")
            val subtitle: String? = null,
            @SerialName("description")
            val description: String? = null,
            @SerialName("authors")
            val authors: List<String>? = null,
            @SerialName("publishedDate")
            val publishedDate: String? = null,
            @SerialName("publisher")
            val publisher: String? = null,
            @SerialName("imageLinks")
            val imageLinks: ImageLinksRemoteModel? = null,
            @SerialName("industryIdentifiers")
            val industryIdentifiers: List<IndustryIdentifier>? = null,
            @SerialName("pageCount")
            val pageCount: Int? = null,
            @SerialName("categories")
            val categories: List<String>? = null,
            @SerialName("averageRating")
            val averageRating: Double? = null,
            @SerialName("ratingsCount")
            val ratingsCount: Int? = null,
            @SerialName("searchInfo")
            val searchInfo: SearchInfo? = null
        ) {
            @Serializable
            data class ImageLinksRemoteModel(
                @SerialName("smallThumbnail")
                val smallThumbnail: String,
                @SerialName("thumbnail")
                val thumbnail: String
            )

            @Serializable
            data class IndustryIdentifier(
                @SerialName("type")
                val type: String,
                @SerialName("identifier")
                val identifier: String
            )

            @Serializable
            data class SearchInfo(
                @SerialName("textSnippet")
                val textSnippet: String
            )
        }
    }
}