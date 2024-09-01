package ui.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.repository.CollectedBookRepository
import data.repository.UserRepository
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import ui.foundation.started

class DashboardViewModel(
    userRepository: UserRepository,
    collectedBookRepository: CollectedBookRepository,
) : ViewModel() {
    val uiState = combine(
        userRepository.get(),
        collectedBookRepository.getRecentReadingBooks(),
        collectedBookRepository.getRecentReadBooks(),
        collectedBookRepository.getRecentCollectBooks()
    ) { user, recentReadingBooks, recentReadBooks, recentCollectedBooks ->
        DashboardUiState.Initialized(
            avatarUrl = user.avatarUrl,
            displayName = user.displayName,
            recentReadingBooks = recentReadingBooks.toImmutableList(),
            recentReadBooks = recentReadBooks.toImmutableList(),
            recentCollectedBooks = recentCollectedBooks.toImmutableList(),
        )
    }.stateIn(viewModelScope, started, DashboardUiState.Initializing)
}