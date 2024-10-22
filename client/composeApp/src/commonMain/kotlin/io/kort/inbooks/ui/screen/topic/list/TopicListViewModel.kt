package io.kort.inbooks.ui.screen.topic.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.kort.inbooks.domain.repository.TopicRepository
import io.kort.inbooks.ui.foundation.started
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn

class TopicListViewModel(
    topicRepository: TopicRepository,
) : ViewModel() {
    val uiState = topicRepository.getAll().mapLatest { topics ->
        TopicListUiState.Initialized(
            intentTo = ::handleIntent,
            topics = topics
        )
    }.stateIn(
        scope = viewModelScope,
        started = started,
        initialValue = TopicListUiState.Initializing
    )

    private fun handleIntent(intent: TopicListUiIntent) {

    }
}