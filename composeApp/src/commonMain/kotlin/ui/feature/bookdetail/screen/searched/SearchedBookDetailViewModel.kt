package ui.feature.bookdetail.screen.searched

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.repository.CollectedBookRepository
import data.repository.SearchBookRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ui.foundation.started

class SearchedBookDetailViewModel(
    id: String,
    searchBookRepository: SearchBookRepository,
    private val collectedBookRepository: CollectedBookRepository,
) : ViewModel() {
    val uiState = flow {
        val book = searchBookRepository.get(id)
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
                    val insertedBookId = collectedBookRepository.insert(book)
                    _uiEvent.emit(SearchedBookDetailUiEvent.Collected(insertedBookId))
                }
            }
        }
    }
}