package io.kort.inbooks.ui.screen.topic.edit.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.kort.inbooks.domain.repository.CollectedBookRepository
import io.kort.inbooks.domain.repository.TopicRepository
import io.kort.inbooks.domain.model.topic.Topic
import io.kort.inbooks.ui.foundation.started
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlin.uuid.Uuid

class AddOrEditTopicViewModel(
    private val topicIdForEdit: String?,
    private val topicRepository: TopicRepository,
    collectedBookRepository: CollectedBookRepository,
) : ViewModel() {
    private val currentTopicStateFlow: MutableStateFlow<Topic?> = MutableStateFlow(null)

    val uiState: StateFlow<AddOrEditTopicUiState> = flow {
        // Load topic for edit
        if (topicIdForEdit != null) {
            val topicOrNull = topicRepository.get(topicIdForEdit).first()
            if (topicOrNull != null) {
                currentTopicStateFlow.value = topicOrNull
            } else {
                _uiEvent.emit(AddOrEditTopicUiEvent.TopicNotFound)
            }
        } else {
            currentTopicStateFlow.value = getDefaultTopic()
        }

        val bookIdToCollectedBooksFlow = currentTopicStateFlow
            .mapLatest { it?.books.orEmpty().map { topicBook -> topicBook.book.id } }
            .distinctUntilChanged()
            .mapLatest { collectedBookRepository.getBookIdToCollectedBook(it) }

        combine(
            currentTopicStateFlow.filterNotNull().distinctUntilChanged(),
            bookIdToCollectedBooksFlow
        ) { topic, bookIdToCollectedBooks ->
            if (topicIdForEdit != null) {
                AddOrEditTopicUiState.Edit.Initialized(
                    intentTo = ::handleIntent,
                    topic = topic,
                    bookIdToCollectedBooks = bookIdToCollectedBooks,
                )
            } else {
                AddOrEditTopicUiState.Add(
                    intentTo = ::handleIntent,
                    topic = topic,
                    bookIdToCollectedBooks = bookIdToCollectedBooks,
                )
            }
        }.let {
            emitAll(it)
        }
    }.stateIn(
        scope = viewModelScope,
        started = started,
        initialValue = getInitialUiState()
    )

    private val _uiEvent = MutableSharedFlow<AddOrEditTopicUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private fun getDefaultTopic(): Topic {
        return Topic(
            id = Uuid.random().toString(),
            title = "",
            description = "",
            books = emptyList(),
            createdAt = Clock.System.now(),
            modifiedAt = null,
        )
    }

    private fun getInitialUiState(): AddOrEditTopicUiState {
        return if (topicIdForEdit != null) {
            AddOrEditTopicUiState.Edit.Initializing(::handleIntent)
        } else {
            AddOrEditTopicUiState.Add(
                intentTo = ::handleIntent,
                topic = getDefaultTopic(),
                bookIdToCollectedBooks = emptyMap(),
            )
        }
    }

    private fun handleIntent(intent: AddOrEditTopicUiIntent) {
        when (intent) {
            is AddOrEditTopicUiIntent.Confirm -> {
                var currentTopic = currentTopicStateFlow.value ?: return
                viewModelScope.launch {
                    /**
                     * 處理如果 title 是空的狀態
                     */
                    currentTopic = currentTopic.copy(title = currentTopic.title.ifBlank { intent.unnamedTitle })

                    if (topicIdForEdit != null) {
                        topicRepository.update(currentTopic.copy(modifiedAt = Clock.System.now()))
                    } else {
                        topicRepository.insert(currentTopic)
                    }

                    _uiEvent.emit(AddOrEditTopicUiEvent.Confirm)
                }
            }

            is AddOrEditTopicUiIntent.UpdateTitle -> {
                currentTopicStateFlow.update { it?.copy(title = intent.title) }
            }

            is AddOrEditTopicUiIntent.UpdateDescription -> {
                currentTopicStateFlow.update { it?.copy(description = intent.description) }
            }

            is AddOrEditTopicUiIntent.UpdateBooks -> {
                currentTopicStateFlow.update { it?.copy(books = intent.books) }
            }
        }
    }
}