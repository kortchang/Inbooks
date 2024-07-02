package ui.screen.searcheddetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import data.repository.SearchBookRepository
import domain.book.SearchedBook
import kotlinx.coroutines.flow.stateIn
import ui.foundation.started

class SearchedBookDetailViewModel(
    bookId: String,
    searchBookRepository: SearchBookRepository,
) : ViewModel() {
    val uiState = moleculeFlow(RecompositionMode.Immediate) {
        val book by produceState<SearchedBook?>(null) {
            value = searchBookRepository.get(bookId)
        }
        SearchedBookDetailUiState(book = book)
    }.stateIn(viewModelScope, started, SearchedBookDetailUiState(book = null))
}