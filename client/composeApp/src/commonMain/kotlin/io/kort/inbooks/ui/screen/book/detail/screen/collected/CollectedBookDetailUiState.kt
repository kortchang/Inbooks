package io.kort.inbooks.ui.screen.book.detail.screen.collected

import io.kort.inbooks.domain.model.book.CollectedBook

data class CollectedBookDetailUiState(
    val intentTo: (CollectedBookDetailUiIntent) -> Unit,
    val book: CollectedBook?,
)

sealed interface CollectedBookDetailUiIntent {
    data class AddReadingEvent(val page: Int) : CollectedBookDetailUiIntent
    data class UpdateReadingReason(val reason: String) : CollectedBookDetailUiIntent
    data object UnCollect : CollectedBookDetailUiIntent
}

sealed interface CollectedBookDetailUiEvent {
    data object UnCollected : CollectedBookDetailUiEvent
}