package io.kort.inbooks.ui.screen.onboarding

import androidx.lifecycle.ViewModel
import io.kort.inbooks.domain.repository.SettingsRepository

class OnboardingViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    suspend fun markIsOnboarded() {
        settingsRepository.markIsOnboarded()
    }
}