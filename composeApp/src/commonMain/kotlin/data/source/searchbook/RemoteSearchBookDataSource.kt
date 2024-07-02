package data.source.searchbook

import book.composeapp.generated.resources.Res
import co.touchlab.kermit.Logger
import data.source.AppHttpClient
import domain.book.SearchedBook
import secret.Secret
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

interface RemoteSearchBookDataSource {
    suspend fun search(query: String): Result<SearchedBooksRemoteModel>
    suspend fun get(id: String): Result<SearchedBooksRemoteModel.SearchedBookRemoteModel?>
}

class GoogleRemoteSearchBooksDataSource(
    private val engine: HttpClientEngine? = null,
) : RemoteSearchBookDataSource {
    private val client by lazy {
        AppHttpClient(
            engine = engine,
            json = Json { ignoreUnknownKeys = true },
        ) {
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "www.googleapis.com"
                    path("books/v1/")
                    parameters.apply {
                        append("projection", "lite")
                        append("key", Secret.GoogleBookApiKey)
                    }
                }
            }
        }
    }

    override suspend fun search(query: String): Result<SearchedBooksRemoteModel> {
        return kotlin.runCatching {
            client.get("volumes") {
                parameter("q", query)
            }.body<SearchedBooksRemoteModel>()
                .removeImageEdgeEffect()
        }
    }

    override suspend fun get(id: String): Result<SearchedBooksRemoteModel.SearchedBookRemoteModel?> {
        return runCatching {
            client.get("volumes/$id")
                .body<SearchedBooksRemoteModel.SearchedBookRemoteModel>()
                .removeImageEdgeEffect()
        }
    }

    private fun SearchedBooksRemoteModel.removeImageEdgeEffect(): SearchedBooksRemoteModel {
        return copy(
            items = items.orEmpty()
                .filter { searchedBook -> searchedBook.volumeInfo.imageLinks != null }
                .map { searchedBook -> searchedBook.removeImageEdgeEffect() }
        )
    }

    private fun SearchedBooksRemoteModel.SearchedBookRemoteModel.removeImageEdgeEffect(): SearchedBooksRemoteModel.SearchedBookRemoteModel {
        fun String.removeEdgeEffect() = replace("&edge=curl", "")

        return copy(
            volumeInfo = volumeInfo.copy(
                imageLinks = volumeInfo.imageLinks?.copy(
                    smallThumbnail = volumeInfo.imageLinks.smallThumbnail.removeEdgeEffect(),
                    thumbnail = volumeInfo.imageLinks.thumbnail.removeEdgeEffect()
                )
            )
        )
    }
}