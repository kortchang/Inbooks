package io.kort.inbooks.ui.token

import androidx.compose.foundation.LocalIndication
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import io.kamel.image.config.LocalKamelConfig
import io.kort.inbooks.ui.foundation.getKamelConfig
import io.kort.inbooks.ui.token.component.Component
import io.kort.inbooks.ui.token.component.LocalComponent
import io.kort.inbooks.ui.token.system.LocalSystem
import io.kort.inbooks.ui.token.system.System

@Composable
fun AppTheme(isDarkTheme: Boolean = false, content: @Composable () -> Unit) {
    val system = System(
        colors = if (isDarkTheme) System.Colors.dark() else System.Colors.light(),
        spacing = System.Spacing.mobile(),
        shadow = if (isDarkTheme) System.Shadow.dark() else System.Shadow.light(),
    )

    val component = Component.default(system)

    val colorScheme = MaterialTheme.colorScheme.copy(
        primary = system.colors.primary,
        surface = system.colors.onPrimary,
    )

    MaterialTheme(
        colorScheme = colorScheme,
    ) {
        CompositionLocalProvider(
            LocalIndication provides ScaleIndication,
            LocalKamelConfig provides getKamelConfig(),
            LocalSystem provides system,
            LocalComponent provides component,
            LocalContentColor provides system.colors.onSurface,
        ) {
            content()
        }
    }
}