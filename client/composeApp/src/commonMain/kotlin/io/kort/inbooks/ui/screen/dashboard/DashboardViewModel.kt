package io.kort.inbooks.ui.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.kort.inbooks.domain.repository.CollectedBookRepository
import io.kort.inbooks.domain.repository.SettingsRepository
import io.kort.inbooks.domain.repository.TopicRepository
import io.kort.inbooks.domain.repository.UserRepository
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import io.kort.inbooks.ui.foundation.started
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class DashboardViewModel(
    userRepository: UserRepository,
    collectedBookRepository: CollectedBookRepository,
    topicRepository: TopicRepository,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {
    val uiState = combine(
        flow { emit(settingsRepository.isOnboardedSignUp()) },
        userRepository.getIsLoggedIn(),
        collectedBookRepository.getRecentCollectBooks(),
        topicRepository.getRecent(),
    ) { isOnboardedSignUp, isLoggedIn, collectedBooks, topics ->
        DashboardUiState.Initialized(
            intentTo = ::intentTo,
            isOnboardedSignUp = isOnboardedSignUp,
            isLoggedIn = isLoggedIn,
            books = collectedBooks,
            topics = topics,
        )
    }.stateIn(viewModelScope, started, DashboardUiState.Initializing)

    fun intentTo(intent: DashboardUiIntent) {
        when (intent) {
            is DashboardUiIntent.MarkIsOnboardedSignUp -> {
                viewModelScope.launch {
                    settingsRepository.markIsOnboardedSignUp()
                }
            }
        }
    }
}