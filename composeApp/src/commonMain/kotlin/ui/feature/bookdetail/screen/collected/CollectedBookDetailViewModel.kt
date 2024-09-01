package ui.feature.bookdetail.screen.collected

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.repository.CollectedBookRepository
import domain.book.CollectedBook
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import ui.foundation.started

class CollectedBookDetailViewModel(
    private val bookId: String,
    private val collectedBookRepository: CollectedBookRepository,
) : ViewModel() {
    val uiState = collectedBookRepository.get(bookId).mapLatest { book ->
        CollectedBookDetailUiState(::onIntent, book = book)
    }.stateIn(viewModelScope, started, CollectedBookDetailUiState(::onIntent, book = null))

    private var updateReadingReasonJob: Job? = null

    private fun onIntent(intent: CollectedBookDetailUiIntent) {
        when (intent) {
            is CollectedBookDetailUiIntent.AddReadingEvent -> {
                val now = Clock.System.now()
                val readingEvent = CollectedBook.ReadingEvent(
                    bookId = bookId,
                    at = now,
                    page = intent.page,
                    modifiedAt = null,
                    createdAt = now,
                )
                viewModelScope.launch {
                    collectedBookRepository.insertReadingEvent(readingEvent)
                }
            }

            is CollectedBookDetailUiIntent.UpdateReadingReason -> {
                updateReadingReasonJob?.cancel()
                updateReadingReasonJob = viewModelScope.launch {
                    delay(1000)
                    collectedBookRepository.updateReadingReason(bookId, intent.reason)
                }
            }
        }
    }
}