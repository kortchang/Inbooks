package io.kort.inbooks.ui.screen.book.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.kort.inbooks.domain.repository.CollectedBookRepository
import io.kort.inbooks.domain.model.settings.BookListDisplayStyle
import io.kort.inbooks.domain.repository.SettingsRepository
import io.kort.inbooks.ui.foundation.started
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BookListViewModel(
    private val settingsRepository: SettingsRepository,
    collectedBookRepository: CollectedBookRepository,
) : ViewModel() {
    val uiState = combine(
        settingsRepository.getBookListDisplayStyleFlow(),
        collectedBookRepository.getCreatedAtLocalYearAndMonthToCollectionBooks()
    ) { displayStyle, books ->
        BookListUiState.Initialized(
            intentTo = ::intentTo,
            displayStyle = displayStyle ?: BookListDisplayStyle.Grid,
            sortedDate = books.keys.sortedDescending(),
            books = books
        )
    }.stateIn(
        scope = viewModelScope,
        started = started,
        initialValue = BookListUiState.Initializing(::intentTo)
    )

    private fun intentTo(intent: BookListUiIntent) {
        viewModelScope.launch {
            when (intent) {
                is BookListUiIntent.UpdateDisplayStyle -> {
                    settingsRepository.updateBookListDisplayStyle(intent.displayStyle)
                }
            }
        }
    }
}