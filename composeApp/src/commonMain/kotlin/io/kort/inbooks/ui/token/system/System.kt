package io.kort.inbooks.ui.token.system

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import io.kort.inbooks.ui.token.reference.Reference

data class System(
    val colors: Colors,
    val spacing: Spacing,
) {
    companion object {
        fun default() = System(
            colors = Colors.light(),
            spacing = Spacing.mobile()
        )

        val colors: Colors
            @Composable
            @ReadOnlyComposable
            get() = LocalSystem.current.colors

        val spacing: Spacing
            @Composable
            @ReadOnlyComposable
            get() = LocalSystem.current.spacing
    }

    class Colors(
        val surface: Color,
        val onSurface: Color,
        val onSurfaceVariant: Color,
        val background: Color,
        val onBackground: Color,
        val onBackgroundVariant: Color,
        val primary: Color,
        val onPrimary: Color,
        val secondary: Color,
        val onSecondary: Color,
        val onSecondaryVariant: Color,
        val collected: Color,
        val outline: Color,
        val warning: Color,
    ) {
        companion object {
            fun light() = Colors(
                surface = Reference.Colors.Grey.L50,
                onSurface = Reference.Colors.Grey.L800,
                onSurfaceVariant = Reference.Colors.Grey.L300,
                background = Reference.Colors.White,
                onBackground = Reference.Colors.Grey.L700,
                onBackgroundVariant = Reference.Colors.Grey.L300,
                primary = Reference.Colors.Grey.L700,
                onPrimary = Reference.Colors.White,
                secondary = Reference.Colors.Grey.L100,
                onSecondary = Reference.Colors.Grey.L800,
                onSecondaryVariant = Reference.Colors.Grey.L400,
                collected = Reference.Colors.Green.L400,
                outline = Reference.Colors.Grey.L100,
                warning = Reference.Colors.Red.L400,
            )
        }
    }

    class Spacing(
        val pagePadding: Dp
    ) {
        companion object {
            fun mobile() = Spacing(
                pagePadding = 32.dp
            )
        }
    }
}

val LocalSystem = staticCompositionLocalOf { System.default() }