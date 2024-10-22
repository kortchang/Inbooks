package io.kort.inbooks.ui.screen.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import io.kort.inbooks.ui.token.AppTheme

@Composable
fun App() {
    AppTheme(isDarkTheme = isSystemInDarkTheme()) {
        AppScreen()
    }
}