package app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.token.AppTheme

@Composable
@Preview
fun App() {
    AppTheme(isDarkTheme = isSystemInDarkTheme()) {
        AppNavHost()
    }
}