package data.repository

import com.benasher44.uuid.uuid4
import data.source.seachbook.RemoteSearchBookDataSource
import data.source.seachbook.SearchedBooksRemoteModel
import domain.book.Book
import domain.book.SearchedBook
import domain.book.isbn10ToIsbn13
import kotlinx.datetime.LocalDate

interface SearchBookRepository {
    suspend fun search(query: String): List<SearchedBook>
    suspend fun get(id: String): SearchedBook?
}

class DefaultSearchBookRepository(
    private val remote: RemoteSearchBookDataSource
) : SearchBookRepository {
    private fun SearchedBooksRemoteModel.toBooks(): List<SearchedBook> {
        return items?.map { it.toBook() } ?: emptyList()
    }

    private fun SearchedBooksRemoteModel.SearchedBookRemoteModel.toBook(): SearchedBook {
        val isbn13 = volumeInfo.industryIdentifiers?.run {
            firstOrNull { it.type.equals("isbn_13", true) }?.identifier
                ?: firstOrNull { it.type.equals("isbn_10", true) }?.identifier?.let {
                    isbn10ToIsbn13(it)
                }
        }

        return SearchedBook(
            book = Book(
                id = uuid4().toString(),
                source = Book.Source.GoogleBooks,
                idBySource = id,
                isbn13 = isbn13,
                coverUrl = volumeInfo.imageLinks?.thumbnail,
                title = volumeInfo.title,
                subtitle = volumeInfo.subtitle,
                authors = volumeInfo.authors ?: emptyList(),
                description = volumeInfo.description,
                publishedDate = volumeInfo.publishedDate,
                publisher = volumeInfo.publisher,
                pageCount = volumeInfo.pageCount,
                categories = volumeInfo.categories,
            ),
            averageRating = volumeInfo.averageRating,
            ratingsCount = volumeInfo.ratingsCount,
            searchTextSnippet = volumeInfo.searchInfo?.textSnippet
        )
    }

    override suspend fun search(query: String): List<SearchedBook> {
        return remote.search(query).getOrThrow().toBooks()
    }

    override suspend fun get(id: String): SearchedBook? {
        return remote.get(id).getOrThrow()?.toBook()
    }
}