package ui.feature.dashboard

import domain.book.CollectedBook
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

sealed interface DashboardUiState {
    data object Initializing : DashboardUiState
    data class Initialized(
        val avatarUrl: String,
        val displayName: String,
        val recentReadingBooks: ImmutableList<CollectedBook> = persistentListOf(),
        val recentReadBooks: ImmutableList<CollectedBook> = persistentListOf(),
        val recentCollectedBooks: ImmutableList<CollectedBook> = persistentListOf()
    ) : DashboardUiState
}