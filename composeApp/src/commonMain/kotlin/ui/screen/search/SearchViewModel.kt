package ui.screen.search

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import data.repository.SearchBookRepository
import domain.book.SearchedBook
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.stateIn
import ui.foundation.started

class SearchViewModel(
    private val searchBookRepository: SearchBookRepository,
) : ViewModel() {
    val uiState = moleculeFlow(RecompositionMode.Immediate) {
        var searchText by remember { mutableStateOf("") }
        var searchedBooks by remember { mutableStateOf<ImmutableList<SearchedBook>>(persistentListOf()) }
        var isLoading by remember { mutableStateOf(false) }
        LaunchedEffect(searchText) {
            if (searchText.isNotBlank()) {
                try {
                    isLoading = true
                    delay(1000)
                    searchedBooks = searchBookRepository.search(searchText).toImmutableList()
                } finally {
                    isLoading = false
                }
            } else {
                isLoading = false
            }
        }

        SearchUiState(
            intentTo = { intent ->
                when (intent) {
                    is SearchUiIntent.Search -> {
                        searchText = intent.value
                    }
                }
            },
            searchedBooks = searchedBooks,
            isLoading = isLoading,
        )
    }.stateIn(viewModelScope, started, SearchUiState())
}