package io.kort.inbooks.ui.screen.dashboard

import io.kort.inbooks.domain.model.book.CollectedBook
import io.kort.inbooks.domain.model.topic.Topic

sealed interface DashboardUiState {
    data object Initializing : DashboardUiState
    data class Initialized(
        val books: List<CollectedBook> = emptyList(),
        val topics: List<Topic> = emptyList(),
    ) : DashboardUiState
}