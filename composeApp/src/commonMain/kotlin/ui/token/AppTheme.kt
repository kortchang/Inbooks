package ui.token

import androidx.compose.foundation.LocalIndication
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import io.kamel.image.config.LocalKamelConfig
import ui.foundation.getKamelConfig
import ui.token.system.LocalSystemColors
import ui.token.system.LocalSystemSpacing
import ui.token.system.System

@Composable
fun AppTheme(isDarkTheme: Boolean = false, content: @Composable () -> Unit) {
    val systemColors = System.Colors.light()
    val systemSpacing = System.Spacing.mobile()

    val colorScheme = MaterialTheme.colorScheme.copy(
        surface = Color(0xFFF6F6F6)
    )

    MaterialTheme(
        colorScheme = colorScheme,
    ) {
        CompositionLocalProvider(
            LocalIndication provides ScaleIndication,
            LocalKamelConfig provides getKamelConfig(),
            LocalSystemColors provides systemColors,
            LocalSystemSpacing provides systemSpacing,
            LocalContentColor provides systemColors.onSurface,
        ) {
            content()
        }
    }
}