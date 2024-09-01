package ui.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.repository.SearchBookRepository
import domain.book.SearchedBook
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ui.foundation.started

class SearchViewModel(
    private val searchBookRepository: SearchBookRepository,
) : ViewModel() {
    private val searchTextFlow = MutableStateFlow("")
    private val searchedBookFlow = MutableStateFlow<ImmutableList<SearchedBook>>(persistentListOf())
    private val isLoadingFlow = MutableStateFlow(false)

    val uiState = channelFlow {
        launch {
            searchTextFlow.collectLatest { searchText ->
                try {
                    if (searchText.isNotBlank()) {
                        delay(1000)
                        isLoadingFlow.value = true
                        searchedBookFlow.value =
                            searchBookRepository.search(searchText).toImmutableList()
                    }
                } finally {
                    isLoadingFlow.value = false
                }
            }
        }

        launch {
            combine(searchedBookFlow, isLoadingFlow) { searchedBooks, isLoading ->
                SearchUiState(
                    intentTo = { intent ->
                        when (intent) {
                            is SearchUiIntent.Search -> {
                                searchTextFlow.value = intent.value
                            }
                        }
                    },
                    searchedBooks = searchedBooks,
                    isLoading = isLoading,
                )
            }.collect { uiState ->
                send(uiState)
            }
        }
    }.stateIn(viewModelScope, started, SearchUiState())
}