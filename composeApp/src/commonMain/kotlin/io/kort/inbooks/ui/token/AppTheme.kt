package io.kort.inbooks.ui.token

import androidx.compose.foundation.LocalIndication
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import io.kamel.image.config.LocalKamelConfig
import io.kort.inbooks.ui.foundation.getKamelConfig
import io.kort.inbooks.ui.token.component.Component
import io.kort.inbooks.ui.token.component.LocalComponent
import io.kort.inbooks.ui.token.system.LocalSystem
import io.kort.inbooks.ui.token.system.System

@Composable
fun AppTheme(isDarkTheme: Boolean = false, content: @Composable () -> Unit) {
    val system = System(
        colors = System.Colors.light(),
        spacing = System.Spacing.mobile(),
    )

    val component = Component.default(system)

    val colorScheme = MaterialTheme.colorScheme.copy(
        primary = system.colors.primary,
        surface = Color(0xFFF6F6F6)
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