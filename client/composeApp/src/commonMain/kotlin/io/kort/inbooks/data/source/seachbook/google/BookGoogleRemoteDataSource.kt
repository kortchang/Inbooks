package io.kort.inbooks.data.source.seachbook.google

import io.kort.inbooks.common.model.book.BookRemoteModel
import io.kort.inbooks.common.service.book.ISBNConverter
import io.kort.inbooks.data.source.AppHttpClient
import io.kort.inbooks.data.source.seachbook.SearchBookRemoteDataSource
import io.kort.inbooks.domain.model.book.Book
import io.kort.inbooks.domain.repository.SearchBookError
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.path
import kotlinx.serialization.json.Json
import secret.Secret
import kotlin.uuid.Uuid

class BookGoogleRemoteDataSource(
    private val engine: HttpClientEngine? = null,
) : SearchBookRemoteDataSource {
    private val client by lazy {
        AppHttpClient(
            engine = engine,
            json = Json { ignoreUnknownKeys = true },
        ) {
            expectSuccess = true
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "www.googleapis.com"
                    path("books/v1/")
                    parameters.apply {
                        append("key", Secret.GoogleBookApiKey)
                    }
                }
            }

            HttpResponseValidator {
                handleResponseException { exception, _ ->
                    val clientException = exception as? ClientRequestException ?: return@handleResponseException
                    val exceptionResponse = clientException.response
                    if (exceptionResponse.status == HttpStatusCode.TooManyRequests) {
                        val exceptionResponseText = exceptionResponse.bodyAsText()
                        throw SearchBookError.TooManyRequests(exceptionResponse.status.value, exceptionResponseText)
                    }
                }
            }
        }
    }

    override fun isMatchedExternalType(type: Book.ExternalId.Type): Boolean {
        return type == Book.ExternalId.Type.GoogleBookId
    }

    override suspend fun search(query: String): Result<List<BookRemoteModel>> {
        return kotlin.runCatching {
            client.get("volumes") {
                parameter("q", query)
                parameter("projection", "lite")
            }
                .body<BooksGoogleRemoteModel>()
                .removeImageEdgeEffect()
                .items
                .orEmpty()
                .map { it.toCommonRemoteModel() }
        }
    }

    override suspend fun get(externalId: Book.ExternalId): Result<BookRemoteModel?> {
        return runCatching {
            client.get("volumes/${externalId.value}")
                .body<BooksGoogleRemoteModel.BookGoogleRemoteModel>()
                .removeImageEdgeEffect()
                .toCommonRemoteModel()
        }
    }

    private fun BooksGoogleRemoteModel.removeImageEdgeEffect(): BooksGoogleRemoteModel {
        return copy(
            items = items.orEmpty()
                .filter { searchedBook -> searchedBook.volumeInfo.imageLinks != null }
                .map { searchedBook -> searchedBook.removeImageEdgeEffect() }
        )
    }

    private fun BooksGoogleRemoteModel.BookGoogleRemoteModel.removeImageEdgeEffect(): BooksGoogleRemoteModel.BookGoogleRemoteModel {
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

    private fun BooksGoogleRemoteModel.BookGoogleRemoteModel.toCommonRemoteModel(): BookRemoteModel {
        val isbn13 = volumeInfo.industryIdentifiers?.run {
            firstOrNull { it.type.equals("isbn_13", true) }?.identifier
                ?: firstOrNull { it.type.equals("isbn_10", true) }?.identifier?.let {
                    ISBNConverter.convertTo13(it).getOrNull()
                }
        }

        val externalIds = listOfNotNull(
            isbn13?.let { BookRemoteModel.ExternalIdRemoteModel(BookRemoteModel.ExternalIdRemoteModel.Type.ISBN13, it) },
            BookRemoteModel.ExternalIdRemoteModel(BookRemoteModel.ExternalIdRemoteModel.Type.GoogleBookId, id)
        )

        return BookRemoteModel(
            id = Uuid.random().toString(),
            source = BookRemoteModel.Source.GoogleBookApi,
            externalIds = externalIds,
            coverUrl = volumeInfo.imageLinks?.thumbnail,
            title = volumeInfo.title,
            subtitle = volumeInfo.subtitle,
            authors = volumeInfo.authors ?: emptyList(),
            description = volumeInfo.description,
            publishedDate = volumeInfo.publishedDate,
            publisher = volumeInfo.publisher,
            pageCount = volumeInfo.pageCount,
            categories = volumeInfo.categories,
        )
    }
}