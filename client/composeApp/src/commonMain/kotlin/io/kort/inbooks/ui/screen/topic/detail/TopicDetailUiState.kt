package io.kort.inbooks.ui.screen.topic.detail

import io.kort.inbooks.domain.model.book.BookId
import io.kort.inbooks.domain.model.book.CollectedBook
import io.kort.inbooks.domain.model.topic.Topic

sealed interface TopicDetailUiState {
    val topicId: String

    data class Initializing(override val topicId: String) : TopicDetailUiState
    data class Initialized(
        val intentTo: (TopicDetailUiIntent) -> Unit,
        val topic: Topic,
        val bookIdToCollectedBook: Map<BookId, CollectedBook>
    ) : TopicDetailUiState {
        override val topicId: String = topic.id
    }
}

sealed interface TopicDetailUiIntent {
    data object Delete : TopicDetailUiIntent
}

sealed interface TopicDetailUiEvent {
    data object TopicNotExist : TopicDetailUiEvent
    data object TopicDeleted : TopicDetailUiEvent
}