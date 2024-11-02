package io.kort.inbooks.ui.screen.search.addbylink

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.kort.inbooks.domain.repository.CollectedBookRepository
import io.kort.inbooks.domain.model.book.Book
import io.kort.inbooks.domain.repository.GetBookByUrlRepository
import io.kort.inbooks.ui.foundation.started
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddByLinkViewModel(
    private val getBookByUrlRepository: GetBookByUrlRepository,
    private val collectedBookRepository: CollectedBookRepository,
) : ViewModel() {
    private val analyzingLinkFlow = MutableStateFlow("")
    val uiState = analyzingLinkFlow.flatMapLatest { link ->
        if (link.isBlank()) {
            flowOf(AddByLinkUiState.Entering(::intentTo))
        } else {
            flow<AddByLinkUiState> {
                emit(AddByLinkUiState.Analyzing(::intentTo))
                getBookByUrl(link)
                    .onSuccess { emit(AddByLinkUiState.AnalyzedSuccess(::intentTo, book = it)) }
                    .onFailure { emit(AddByLinkUiState.AnalyzedFailed(::intentTo)) }
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = started,
        initialValue = AddByLinkUiState.Entering(::intentTo)
    )

    private val _uiEvent = MutableSharedFlow<AddByLinkUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private fun intentTo(intent: AddByLinkUiIntent) {
        when (intent) {
            is AddByLinkUiIntent.Analyze -> {
                analyzingLinkFlow.update { intent.link }
            }

            AddByLinkUiIntent.BackToEnter -> {
                viewModelScope.launch {
                    analyzingLinkFlow.update { "" }
                    _uiEvent.emit(AddByLinkUiEvent.ClearUrl)
                }
            }

            AddByLinkUiIntent.Collect -> {
                viewModelScope.launch {
                    val book = (uiState.value as? AddByLinkUiState.AnalyzedSuccess)?.book ?: return@launch
                    val bookId = collectedBookRepository.insert(book)
                    _uiEvent.emit(AddByLinkUiEvent.Collected(bookId))
                }
            }
        }
    }

    private suspend fun getBookByUrl(url: String): Result<Book> {
        return getBookByUrlRepository.get(url)
    }
}