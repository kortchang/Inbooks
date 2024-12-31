package io.kort.inbooks.ui.screen.auth.login.email

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.kort.inbooks.ui.foundation.started
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LoginByEmailViewModel : ViewModel() {
    private val emailFlow = MutableStateFlow("")
    private val passwordFlow = MutableStateFlow("")
    private val isLoadingFlow = MutableStateFlow(false)
    private val errorFlow = MutableStateFlow<LoginByEmailUiState.Error?>(null)

    val uiState = channelFlow<LoginByEmailUiState> {
        // 只要有改變，就把 error 清除
        launch {
            combine(emailFlow, passwordFlow) { _, _ -> errorFlow.value = null }.collect()
        }

        launch {
            combine(emailFlow, passwordFlow, isLoadingFlow, errorFlow) { email, password, isLoading, error ->
                LoginByEmailUiState(
                    intentTo = ::intentTo,
                    confirmButtonUiState = getConfirmButtonUiState(isLoading, error, email, password),
                    error = error,
                )
            }
        }
    }.stateIn(
        viewModelScope,
        started,
        LoginByEmailUiState(
            intentTo = ::intentTo,
            confirmButtonUiState = LoginByEmailUiState.ConfirmButtonUiState.Enabled,
            error = null,
        )
    )

    private fun getConfirmButtonUiState(
        isLoading: Boolean,
        error: LoginByEmailUiState.Error?,
        email: String,
        password: String
    ): LoginByEmailUiState.ConfirmButtonUiState {
        return if (isLoading) {
            LoginByEmailUiState.ConfirmButtonUiState.Loading
        } else if (email.isBlank() || password.isBlank() || error != null) {
            LoginByEmailUiState.ConfirmButtonUiState.Disabled
        } else {
            LoginByEmailUiState.ConfirmButtonUiState.Enabled
        }
    }

    private fun intentTo(intent: LoginByEmailUiIntent) {
        when (intent) {
            is LoginByEmailUiIntent.Confirm -> {

            }

            is LoginByEmailUiIntent.UpdateEmail -> emailFlow.value = intent.email
            is LoginByEmailUiIntent.UpdatePassword -> passwordFlow.value = intent.password
        }
    }
}