package io.kort.inbooks.ui.screen.topic.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.kort.inbooks.domain.repository.CollectedBookRepository
import io.kort.inbooks.domain.repository.TopicRepository
import io.kort.inbooks.ui.foundation.started
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TopicDetailViewModel(
    private val topicId: String,
    private val topicRepository: TopicRepository,
    collectedBookRepository: CollectedBookRepository,
) : ViewModel() {
    val uiState = topicRepository.get(topicId).flatMapLatest { topic ->
        if (topic == null) {
            _uiEvent.emit(TopicDetailUiEvent.TopicNotExist)
            flowOf(TopicDetailUiState.Initializing(topicId))
        } else {
            collectedBookRepository
                .getBookIdToCollectedBookFlow(topic.books.map { it.book.id })
                .mapLatest { bookIdToCollectedBook ->
                    TopicDetailUiState.Initialized(
                        intentTo = ::handleIntent,
                        topic = topic,
                        bookIdToCollectedBook = bookIdToCollectedBook,
                    )
                }
        }
    }.stateIn(
        viewModelScope,
        started,
        TopicDetailUiState.Initializing(topicId),
    )

    private val _uiEvent = MutableSharedFlow<TopicDetailUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private fun handleIntent(intent: TopicDetailUiIntent) {
        viewModelScope.launch {
            when (intent) {
                TopicDetailUiIntent.Delete -> {
                    topicRepository.delete(topicId)
                    _uiEvent.emit(TopicDetailUiEvent.TopicDeleted)
                }
            }
        }
    }
}