package io.kort.inbooks.ui.screen.search

import androidx.compose.runtime.Stable
import io.kort.inbooks.domain.model.book.Book
import io.kort.inbooks.domain.model.book.CollectedBook
import io.kort.inbooks.domain.model.book.SearchedBook

@Stable
data class SearchUiState(
    val intentTo: (SearchUiIntent) -> Unit = {},
    val books: List<BookUiModel> = emptyList(),
    val query: String = "",
    val isSearching: Boolean = false,
) {
    sealed interface BookUiModel {
        val book: Book

        data class SearchedBookUiModel(val searchedBook: SearchedBook) : BookUiModel {
            override val book: Book = searchedBook.book
        }

        data class CollectedBookUiModel(val collectedBook: CollectedBook) : BookUiModel {
            override val book: Book = collectedBook.book
        }
    }
}

sealed interface SearchUiIntent {
    data class Search(val value: String) : SearchUiIntent
}

sealed interface SearchUiEvent {
    data object TooManyRequestsError : SearchUiEvent
}