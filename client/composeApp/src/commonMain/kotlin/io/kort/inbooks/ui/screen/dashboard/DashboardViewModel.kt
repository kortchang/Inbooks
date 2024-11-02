package io.kort.inbooks.ui.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.kort.inbooks.domain.repository.CollectedBookRepository
import io.kort.inbooks.domain.repository.TopicRepository
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import io.kort.inbooks.ui.foundation.started

class DashboardViewModel(
    collectedBookRepository: CollectedBookRepository,
    topicRepository: TopicRepository,
) : ViewModel() {
    val uiState = combine(
        collectedBookRepository.getRecentCollectBooks(),
        topicRepository.getRecent(),
    ) { collectedBooks, topics ->
        DashboardUiState.Initialized(
            books = collectedBooks,
            topics = topics,
        )
    }.stateIn(viewModelScope, started, DashboardUiState.Initializing)
}