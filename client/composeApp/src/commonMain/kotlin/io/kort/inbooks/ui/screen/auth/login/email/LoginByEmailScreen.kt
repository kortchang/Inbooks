package io.kort.inbooks.ui.screen.auth.login.email

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import io.kort.inbooks.ui.component.Button
import io.kort.inbooks.ui.component.ButtonDefaults
import io.kort.inbooks.ui.component.ButtonDefaults.loadingIndicator
import io.kort.inbooks.ui.component.PageScope
import io.kort.inbooks.ui.component.TextField
import io.kort.inbooks.ui.component.TextFieldDefaults
import io.kort.inbooks.ui.foundation.thenIf
import io.kort.inbooks.ui.resource.Icons
import io.kort.inbooks.ui.resource.LogIn
import io.kort.inbooks.ui.resource.NavArrowLeft
import io.kort.inbooks.ui.resource.NavArrowRight
import io.kort.inbooks.ui.resource.Res
import io.kort.inbooks.ui.resource.auth_login_error_wrong_email_or_password
import io.kort.inbooks.ui.resource.auth_login_title
import io.kort.inbooks.ui.resource.email
import io.kort.inbooks.ui.resource.login
import io.kort.inbooks.ui.screen.auth.common.AuthScreenDefaults
import io.kort.inbooks.ui.token.system.System
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinNavViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun PageScope.LoginByEmailScreen(back: () -> Unit, modifier: Modifier = Modifier) {
    val viewModel: LoginByEmailViewModel = koinNavViewModel()
    val uiState by viewModel.uiState.collectAsState()
    Column(modifier.windowInsetsPadding(windowInsets)) {
        TopAppBar(onBackButtonClick = back)
        val scrollState = rememberScrollState()
        Column(Modifier.weight(1f).verticalScroll(scrollState)) {
            Spacer(Modifier.height(80.dp))
            AuthScreenDefaults.Title(icon = Icons.LogIn, title = stringResource(Res.string.auth_login_title))
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            Form(
                email = email,
                onEmailChange = {
                    email = it
                    uiState.intentTo(LoginByEmailUiIntent.UpdateEmail(it))
                },
                password = password,
                onPasswordChange = {
                    password = it
                    uiState.intentTo(LoginByEmailUiIntent.UpdatePassword(it))
                },
                error = uiState.error
            )
            Spacer(Modifier.weight(1f))
            Button(
                modifier = Modifier.fillMaxWidth()
                    .thenIf(uiState.confirmButtonUiState == LoginByEmailUiState.ConfirmButtonUiState.Loading) {
                        loadingIndicator(System.colors.onSurfaceVariant)
                    },
                enabled = uiState.confirmButtonUiState == LoginByEmailUiState.ConfirmButtonUiState.Enabled,
                onClick = { uiState.intentTo(LoginByEmailUiIntent.Confirm(email, password)) },
                text = { Text(stringResource(Res.string.login)) },
                end = { Icon(Icons.NavArrowRight, contentDescription = null) },
            )
        }
    }
}

@Composable
fun Form(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    error: LoginByEmailUiState.Error?
) {
    // Add autofill
    TextField(
        initialValue = email,
        onValueChange = onEmailChange,
        singleLine = true,
        placeholder = stringResource(Res.string.email),
        additional = {
            when (error) {
                LoginByEmailUiState.Error.InvalidEmailOrPassword -> {
                    TextFieldDefaults.WarningText(
                        stringResource(Res.string.auth_login_error_wrong_email_or_password)
                    )
                }

                null -> {}
            }
        }
    )

    // Add autofill
    TextField(
        initialValue = password,
        onValueChange = onPasswordChange,
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        additional = {
            when (error) {
                LoginByEmailUiState.Error.InvalidEmailOrPassword -> {
                    TextFieldDefaults.WarningText(
                        stringResource(Res.string.auth_login_error_wrong_email_or_password)
                    )
                }

                null -> {}
            }
        }
    )
}

@Composable
private fun TopAppBar(onBackButtonClick: () -> Unit) {
    io.kort.inbooks.ui.component.TopAppBar(
        start = {
            Button(
                onClick = onBackButtonClick,
                colors = ButtonDefaults.backgroundButtonColors(),
                start = { Icon(Icons.NavArrowLeft, contentDescription = null) }
            )
        }
    )
}