package io.kort.inbooks.ui.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.kort.inbooks.domain.model.book.CollectedBook
import io.kort.inbooks.domain.model.book.SearchedBook
import io.kort.inbooks.domain.repository.SearchBookRepository
import io.kort.inbooks.domain.usecase.ResolveSearchedBooksToCollectedOrNotUseCase
import io.kort.inbooks.ui.foundation.started
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchBookRepository: SearchBookRepository,
    private val resolveSearchedBooksToCollectedOrNotUseCase: ResolveSearchedBooksToCollectedOrNotUseCase,
) : ViewModel() {
    private val searchTextFlow = MutableStateFlow("")
    private val searchedBookUiModelsFlow = MutableStateFlow<List<SearchUiState.BookUiModel>>(listOf())
    private val isSearchingFlow = MutableStateFlow(false)

    val uiState = channelFlow {
        launch {
            searchTextFlow.collectLatest { searchText ->
                try {
                    if (searchText.isNotBlank()) {
                        delay(1000)
                        isSearchingFlow.value = true
                        searchedBookUiModelsFlow.value = getBookUiModels(searchText)
                            .onFailure { _uiEvent.emit(SearchUiEvent.TooManyRequestsError) }
                            .getOrDefault(emptyList())
                    }
                } finally {
                    isSearchingFlow.value = false
                }
            }
        }

        launch {
            combine(
                searchTextFlow,
                searchedBookUiModelsFlow,
                isSearchingFlow
            ) { searchText, searchedBooks, isSearching ->
                SearchUiState(
                    intentTo = { intent ->
                        when (intent) {
                            is SearchUiIntent.Search -> {
                                searchTextFlow.value = intent.value
                            }
                        }
                    },
                    query = searchText,
                    books = searchedBooks,
                    isSearching = isSearching,
                )
            }.collect { uiState ->
                send(uiState)
            }
        }
    }.stateIn(viewModelScope, started, SearchUiState())

    private val _uiEvent = MutableSharedFlow<SearchUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private suspend fun getBookUiModels(searchText: String): Result<List<SearchUiState.BookUiModel>> {
        return searchBookRepository.search(searchText).map {
            resolveSearchedBooksToCollectedOrNotUseCase(it).mapNotNull { books ->
                when (books) {
                    is SearchedBook -> SearchUiState.BookUiModel.SearchedBookUiModel(books)
                    is CollectedBook -> SearchUiState.BookUiModel.CollectedBookUiModel(books)
                    else -> null
                }
            }
        }
    }
}