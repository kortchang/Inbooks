package io.kort.inbooks.ui.screen.book.list

import io.kort.inbooks.domain.model.book.CollectedBook
import io.kort.inbooks.domain.model.book.LocalYearAndMonth
import io.kort.inbooks.domain.model.settings.BookListDisplayStyle

sealed interface BookListUiState {
    val intentTo: (BookListUiIntent) -> Unit

    data class Initializing(
        override val intentTo: (BookListUiIntent) -> Unit,
    ) : BookListUiState

    data class Initialized(
        override val intentTo: (BookListUiIntent) -> Unit,
        val displayStyle: BookListDisplayStyle,
        val sortedDate: List<LocalYearAndMonth>,
        val books: Map<LocalYearAndMonth, List<CollectedBook>>,
    ) : BookListUiState
}

sealed interface BookListUiIntent {
    data class UpdateDisplayStyle(val displayStyle: BookListDisplayStyle): BookListUiIntent
}