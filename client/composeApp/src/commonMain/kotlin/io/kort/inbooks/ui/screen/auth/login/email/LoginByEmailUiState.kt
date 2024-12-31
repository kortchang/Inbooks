package io.kort.inbooks.ui.screen.auth.login.email

data class LoginByEmailUiState(
    val intentTo: (LoginByEmailUiIntent) -> Unit,
    val confirmButtonUiState: ConfirmButtonUiState,
    val error: Error?,
) {
    enum class ConfirmButtonUiState {
        Enabled,
        Loading,
        Disabled,
    }

    enum class Error {
        InvalidEmailOrPassword,
    }
}

sealed interface LoginByEmailUiIntent {
    data class UpdateEmail(val email: String) : LoginByEmailUiIntent
    data class UpdatePassword(val password: String) : LoginByEmailUiIntent
    data class Confirm(val email: String, val password: String) : LoginByEmailUiIntent
}
