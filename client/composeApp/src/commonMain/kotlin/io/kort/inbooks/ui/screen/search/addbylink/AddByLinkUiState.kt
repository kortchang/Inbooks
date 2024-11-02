package io.kort.inbooks.ui.screen.search.addbylink

import io.kort.inbooks.domain.model.book.Book
import io.kort.inbooks.domain.model.book.BookId
import io.kort.inbooks.domain.model.book.CollectedBook
import io.kort.inbooks.domain.model.book.SearchedBook

sealed interface AddByLinkUiState {
    val intentTo: (AddByLinkUiIntent) -> Unit

    data class Entering(override val intentTo: (AddByLinkUiIntent) -> Unit) : AddByLinkUiState
    data class Analyzing(override val intentTo: (AddByLinkUiIntent) -> Unit) : AddByLinkUiState
    data class AnalyzedFailed(override val intentTo: (AddByLinkUiIntent) -> Unit) : AddByLinkUiState
    data class AnalyzedSuccess(override val intentTo: (AddByLinkUiIntent) -> Unit, val book: Book) :
        AddByLinkUiState
}

sealed interface AddByLinkUiIntent {
    data class Analyze(val link: String) : AddByLinkUiIntent
    data object Collect : AddByLinkUiIntent
    data object BackToEnter : AddByLinkUiIntent
}

sealed interface AddByLinkUiEvent {
    data object ClearUrl : AddByLinkUiEvent
    data class Collected(val collectedBookId: BookId) : AddByLinkUiEvent
}