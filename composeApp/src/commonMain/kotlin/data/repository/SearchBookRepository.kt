package data.repository

import data.source.searchbook.RemoteSearchBookDataSource
import data.source.searchbook.SearchedBooksRemoteModel
import domain.book.SearchedBook

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
        return SearchedBook(
            id = id,
            coverUrl = volumeInfo.imageLinks?.thumbnail,
            name = volumeInfo.title,
            authors = volumeInfo.authors ?: emptyList(),
            description = volumeInfo.description
        )
    }

    override suspend fun search(query: String): List<SearchedBook> {
        return remote.search(query).getOrThrow().toBooks()
    }

    override suspend fun get(id: String): SearchedBook? {
        return remote.get(id).getOrThrow()?.toBook()
    }
}