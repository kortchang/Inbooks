package io.kort.inbooks.common.service.book

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.model.MetaData
import com.fleeksoft.ksoup.network.NetworkHelperKtor
import com.fleeksoft.ksoup.network.parseGetRequest
import com.fleeksoft.ksoup.nodes.Document
import com.fleeksoft.ksoup.nodes.Element
import io.kort.inbooks.common.model.book.BookRemoteModel
import io.ktor.client.plugins.retry
import io.ktor.http.Url
import io.ktor.http.isSuccess
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * books.com.tw
 */
class BooksUrlAnalyzer : BookUrlAnalyzer {
    override suspend fun canAnalyze(url: String): Boolean {
        return runCatching { Url(url).host == "www.books.com.tw" }.getOrDefault(false)
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun analyze(url: String): BookRemoteModel? {
        val urlWithParameter = url.replace("""\?.*""".toRegex(), "")

        val document = Ksoup.parseGetRequest(url = urlWithParameter)
        val metadata = Ksoup.parseMetaData(document)

        val title = metadata.ogTitle ?: metadata.title ?: return null

        val information = metadata.getInformation(title)

        return BookRemoteModel(
            id = Uuid.random().toString(),
            source = BookRemoteModel.Source.BooksUrl,
            externalIds = getExternalIds(metadata, information, urlWithParameter),
            coverUrl = metadata.getCoverImage(),
            title = title,
            subtitle = null,
            description = document.getDescription(),
            authors = information?.get(BookInformationKey.Authors)?.split(","),
            publishedDate = information?.get(BookInformationKey.PublishedDate),
            publisher = information?.get(BookInformationKey.Publisher),
            pageCount = information?.get(BookInformationKey.PageCount)?.toIntOrNull(),
            categories = information?.get(BookInformationKey.Categories)?.split(","),
        )
    }
}

private fun getExternalIds(
    metadata: MetaData,
    information: Map<BookInformationKey, String>?,
    givenUrl: String
): List<BookRemoteModel.ExternalIdRemoteModel> {
    val url = BookRemoteModel.ExternalIdRemoteModel(
        type = BookRemoteModel.ExternalIdRemoteModel.Type.BooksUrl,
        value = metadata.ogUrl ?: givenUrl
    )

    val isbn13 = information?.get(BookInformationKey.ISBN)?.let { isbn ->
        ISBNConverter.convertTo13(isbn).getOrNull()?.let { isbn13 ->
            BookRemoteModel.ExternalIdRemoteModel(
                type = BookRemoteModel.ExternalIdRemoteModel.Type.ISBN13,
                value = isbn13
            )
        }
    }

    return listOfNotNull(url, isbn13)
}

private suspend fun MetaData.getCoverImage(): String? {
    val imageUrl = ogImage ?: return null
    val flattenCover = imageUrl
        .replace(regex = """\.jpg""".toRegex(), "_bc_01.jpg")
        .replace(regex = """&v=[A-Za-z0-9]+&w=[0-9]+&h=[0-9]+""".toRegex(), "&h=600")

    return if (NetworkHelperKtor.instance.get(flattenCover).status.isSuccess()) {
        flattenCover
    } else {
        imageUrl
            .replace(regex = """&v=[A-Za-z0-9]+&w=[0-9]+&h=[0-9]+""".toRegex(), "&h=600")
    }
}

private fun MetaData.getInformation(title: String): Map<BookInformationKey, String>? {
    return ogDescription?.let { description ->
        buildMap {
            description
                .replace("書名：$title，", "")
                .split("，")
                .forEach { information ->
                    runCatching {
                        val (key, value) = information.split("：")
                        val informationKey = BookInformationKey.entries.firstOrNull { it.key == key }
                        if (informationKey != null) {
                            put(informationKey, value)
                        }
                    }
                }
        }
    }
}

enum class BookInformationKey(val key: String) {
    // 沒有書名是因為從 ogTitle 就拿得到，而且他可能因為有“，”字元而被拆分。
    Language("語言"),
    ISBN("ISBN"),
    PageCount("頁數"),
    Publisher("出版社"),
    Authors("作者"),
    PublishedDate("出版日期"),
    Categories("類別"),
}

private fun Document.getDescription(): String? {
    return body()
        .findChildrenElementMatchingText("內容簡介".toRegex())
        ?.parent()
        ?.children()
        ?.findLast { it.hasClass("bd") }
        ?.child(0)
        ?.html()
}

private fun Element.findChildrenElementMatchingText(regex: Regex): Element? {
    if (childrenSize() == 0) {
        return null
    } else {
        children().forEach { child ->
            if (regex.matches(child.text())) {
                return child
            } else {
                child.findChildrenElementMatchingText(regex)?.let { return it }
            }
        }
    }

    return null
}