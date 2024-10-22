package io.kort.inbooks.ui.screen.topic.edit.main

import io.kort.inbooks.domain.model.book.BookId
import io.kort.inbooks.domain.model.book.CollectedBook
import io.kort.inbooks.domain.model.topic.Topic
import io.kort.inbooks.domain.model.topic.TopicBook

sealed interface AddOrEditTopicUiState {
    val intentTo: (AddOrEditTopicUiIntent) -> Unit
    val confirmEnable: Boolean

    data class Add(
        override val intentTo: (AddOrEditTopicUiIntent) -> Unit,
        override val topic: Topic,
        override val bookIdToCollectedBooks: Map<BookId, CollectedBook>,
    ) : AddOrEditTopicUiState, InitializedUiState {
        override val confirmEnable: Boolean
            get() = topic.title.isNotBlank() || topic.description.isNotBlank() || topic.books.isNotEmpty()
    }

    sealed interface Edit : AddOrEditTopicUiState {
        data class Initializing(override val intentTo: (AddOrEditTopicUiIntent) -> Unit) : Edit {
            override val confirmEnable: Boolean = false
        }

        data class Initialized(
            override val intentTo: (AddOrEditTopicUiIntent) -> Unit,
            override val topic: Topic,
            override val bookIdToCollectedBooks: Map<BookId, CollectedBook>,
        ) : Edit, InitializedUiState {
            override val confirmEnable: Boolean
                get() = topic.title.isNotBlank() || topic.description.isNotBlank() || topic.books.isNotEmpty()
        }
    }

    interface InitializedUiState : Edit {
        val topic: Topic
        val bookIdToCollectedBooks: Map<BookId, CollectedBook>
    }
}

sealed interface AddOrEditTopicUiIntent {
    data class Confirm(val unnamedTitle: String) : AddOrEditTopicUiIntent
    data class UpdateTitle(val title: String) : AddOrEditTopicUiIntent
    data class UpdateDescription(val description: String) : AddOrEditTopicUiIntent
    data class UpdateBooks(val books: List<TopicBook>) : AddOrEditTopicUiIntent
}

sealed interface AddOrEditTopicUiEvent {
    data object TopicNotFound : AddOrEditTopicUiEvent
    data object Confirm : AddOrEditTopicUiEvent
}