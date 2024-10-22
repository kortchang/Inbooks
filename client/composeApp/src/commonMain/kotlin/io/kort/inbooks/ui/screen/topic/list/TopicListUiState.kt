package io.kort.inbooks.ui.screen.topic.list

import io.kort.inbooks.domain.model.topic.Topic

sealed interface TopicListUiState {
    data object Initializing : TopicListUiState
    data class Initialized(
        val intentTo: (TopicListUiIntent) -> Unit,
        val topics: List<Topic>
    ): TopicListUiState
}

sealed interface TopicListUiIntent {

}