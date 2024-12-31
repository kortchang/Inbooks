package io.kort.inbooks.ui.screen.dashboard

import io.kort.inbooks.domain.model.book.CollectedBook
import io.kort.inbooks.domain.model.topic.Topic

sealed interface DashboardUiState {
    data object Initializing : DashboardUiState
    data class Initialized(
        val intentTo: (DashboardUiIntent) -> Unit,
        val isLoggedIn: Boolean,
        val isOnboardedSignUp: Boolean,
        val books: List<CollectedBook> = emptyList(),
        val topics: List<Topic> = emptyList(),
    ) : DashboardUiState {
        val showRedDotOnAvatarToOnboardSignUp: Boolean = isOnboardedSignUp.not() && isLoggedIn.not()
    }
}

sealed interface DashboardUiIntent {
    data object MarkIsOnboardedSignUp : DashboardUiIntent
}