package io.kort.inbooks.ui.screen.book.detail.screen.searched

import io.kort.inbooks.domain.model.book.SearchedBook

data class SearchedBookDetailUiState(
    val intentTo: (SearchedBookDetailUiIntent) -> Unit,
    val book: SearchedBook?,
)

sealed interface SearchedBookDetailUiIntent {
    data object Collect : SearchedBookDetailUiIntent
}

sealed interface SearchedBookDetailUiEvent {
    data object BookNotExist : SearchedBookDetailUiEvent
    data class Collected(val collectedBookId: String) : SearchedBookDetailUiEvent
}