package ui.feature.bookdetail.screen.searched

import domain.book.SearchedBook

data class SearchedBookDetailUiState(
    val intentTo: (SearchedBookDetailUiIntent) -> Unit,
    val book: SearchedBook?,
)

sealed interface SearchedBookDetailUiIntent {
    data object Collect : SearchedBookDetailUiIntent
}

sealed interface SearchedBookDetailUiEvent {
    data class Collected(val collectedBookId: String) : SearchedBookDetailUiEvent
}