package io.kort.inbooks.ui.screen.app

import androidx.lifecycle.ViewModel
import io.kort.inbooks.domain.repository.SettingsRepository

class AppViewModel(
    private val settingsRepository: SettingsRepository,
): ViewModel() {
    suspend fun isOnboarded(): Boolean {
        return settingsRepository.isOnboarded()
    }
}