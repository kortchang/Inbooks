package io.kort.inbooks.ui.screen.topic.edit.book

import io.kort.inbooks.domain.model.book.Book
import io.kort.inbooks.domain.model.book.BookId
import io.kort.inbooks.domain.model.book.CollectedBook
import io.kort.inbooks.domain.model.book.LocalYearAndMonth
import io.kort.inbooks.domain.model.topic.TopicBook

sealed interface SelectBookUiState {
    data object Initializing : SelectBookUiState
    data class Initialized(
        val intentTo: (SelectBookUiIntent) -> Unit,
        val books: Map<LocalYearAndMonth, List<Book>>,
        val bookIdToCollectedBooks: Map<BookId, CollectedBook>,
        val selectedBooks: List<TopicBook>,
    )
}

sealed interface SelectBookUiIntent {
    data class UpdateBookSelected(val book: Book, val targetSelected: Boolean) : SelectBookUiIntent
}