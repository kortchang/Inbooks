package io.kort.inbooks.ui.screen.topic.edit.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import io.kort.inbooks.data.repository.CollectedBookRepository
import io.kort.inbooks.domain.model.topic.TopicBook
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import io.kort.inbooks.ui.foundation.started
import kotlinx.datetime.Clock

class SelectBookViewModel(
    private val initialSelectedBooks: List<TopicBook>,
    collectedBookRepository: CollectedBookRepository,
) : ViewModel() {
    private val currentSelectedBooksStateFlow = MutableStateFlow(initialSelectedBooks)

    val uiState = combine(
        currentSelectedBooksStateFlow,
        collectedBookRepository.getCreatedAtLocalYearAndMonthToCollectionBooks(),
    ) { selectedBook, collectedBooks ->
        SelectBookUiState.Initialized(
            intentTo = ::handleIntent,
            books = collectedBooks.mapValues { it.value.map { it.book } },
            bookIdToCollectedBooks = collectedBooks.values.flatten().associateBy { it.book.id },
            selectedBooks = selectedBook,
        )
    }.stateIn(
        scope = viewModelScope,
        started = started,
        initialValue = SelectBookUiState.Initializing,
    )

    private fun handleIntent(intent: SelectBookUiIntent) {
        when (intent) {
            is SelectBookUiIntent.UpdateBookSelected -> {
                val (book, targetSelected) = intent

                val topicBook = initialSelectedBooks.find { it.book.id == book.id }
                    ?: TopicBook(book = book, insertedAt = Clock.System.now())

                currentSelectedBooksStateFlow.update { selectedBooks ->
                    if (targetSelected) {
                        listOf(topicBook) + selectedBooks
                    } else {
                        selectedBooks.filterNot { it.book.id == topicBook.book.id }
                    }
                }

                Logger.d("[Kort Debug]") { "currentSelectedBook: ${currentSelectedBooksStateFlow.value.joinToString { (it.book.title to it.insertedAt).toString() }}" }
            }
        }
    }
}