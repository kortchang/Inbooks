package io.kort.inbooks.ui.screen.book.detail.screen.searched

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.kort.inbooks.data.repository.CollectedBookRepository
import io.kort.inbooks.domain.repository.SearchBookRepository
import io.kort.inbooks.domain.model.book.Book
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import io.kort.inbooks.ui.foundation.started

class SearchedBookDetailViewModel(
    externalIds: List<Book.ExternalId>,
    searchBookRepository: SearchBookRepository,
    private val collectedBookRepository: CollectedBookRepository,
) : ViewModel() {
    val uiState = flow {
        val book = searchBookRepository.get(externalIds)
            .onFailure { _uiEvent.emit(SearchedBookDetailUiEvent.BookNotExist) }
            .getOrNull()
        emit(
            SearchedBookDetailUiState(
                intentTo = ::onIntent,
                book = book,
            )
        )
    }.stateIn(
        viewModelScope,
        started,
        SearchedBookDetailUiState(::onIntent, book = null)
    )

    private val _uiEvent = MutableSharedFlow<SearchedBookDetailUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private fun onIntent(intent: SearchedBookDetailUiIntent) {
        when (intent) {
            SearchedBookDetailUiIntent.Collect -> {
                val book = uiState.value.book ?: return
                viewModelScope.launch {
                    val insertedBookId = collectedBookRepository.insert(book.book)
                    _uiEvent.emit(SearchedBookDetailUiEvent.Collected(insertedBookId))
                }
            }
        }
    }
}