package ui.feature.bookdetail.screen.collected

import domain.book.CollectedBook

data class CollectedBookDetailUiState(
    val intentTo: (CollectedBookDetailUiIntent) -> Unit,
    val book: CollectedBook?,
)

sealed interface CollectedBookDetailUiIntent {
    data class AddReadingEvent(val page: Int) : CollectedBookDetailUiIntent
    data class UpdateReadingReason(val reason: String): CollectedBookDetailUiIntent
}