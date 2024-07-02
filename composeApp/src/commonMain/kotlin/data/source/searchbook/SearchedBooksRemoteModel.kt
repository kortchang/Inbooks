package data.source.searchbook

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchedBooksRemoteModel(
    @SerialName("items")
    val items: List<SearchedBookRemoteModel>?,
    @SerialName("kind")
    val kind: String,
    @SerialName("totalItems")
    val totalItems: Int
) {
    @Serializable
    data class SearchedBookRemoteModel(
        @SerialName("id")
        val id: String,
        @SerialName("kind")
        val kind: String,
        @SerialName("saleInfo")
        val saleInfo: SaleInfoRemoteModel? = null,
        @SerialName("searchInfo")
        val searchInfo: SearchInfoRemoteModel? = null,
        @SerialName("selfLink")
        val selfLink: String,
        @SerialName("volumeInfo")
        val volumeInfo: VolumeInfoRemoteModel
    ) {
        @Serializable
        data class SaleInfoRemoteModel(
            @SerialName("buyLink")
            val buyLink: String? = null,
            @SerialName("country")
            val country: String? = null,
        )

        @Serializable
        data class SearchInfoRemoteModel(
            @SerialName("textSnippet")
            val textSnippet: String
        )

        @Serializable
        data class VolumeInfoRemoteModel(
            @SerialName("authors")
            val authors: List<String>? = null,
            @SerialName("description")
            val description: String? = null,
            @SerialName("imageLinks")
            val imageLinks: ImageLinksRemoteModel? = null,
            @SerialName("publishedDate")
            val publishedDate: String? = null,
            @SerialName("publisher")
            val publisher: String? = null,
            @SerialName("subtitle")
            val subtitle: String? = null,
            @SerialName("title")
            val title: String
        ) {
            @Serializable
            data class ImageLinksRemoteModel(
                @SerialName("smallThumbnail")
                val smallThumbnail: String,
                @SerialName("thumbnail")
                val thumbnail: String
            )
        }
    }
}