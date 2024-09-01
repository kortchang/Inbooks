package ui.feature.search

import androidx.compose.runtime.Stable
import domain.book.SearchedBook
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Stable
data class SearchUiState(
    val intentTo: (SearchUiIntent) -> Unit = {},
    val searchedBooks: ImmutableList<SearchedBook> = persistentListOf(),
    val isLoading: Boolean = false,
)

sealed class SearchUiIntent {
    data class Search(val value: String) : SearchUiIntent()
}