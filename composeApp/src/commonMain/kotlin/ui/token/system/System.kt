package ui.token.system

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ui.token.reference.Reference

object System {
    class Colors(
        val surface: Color,
        val onSurface: Color,
        val onSurfaceVariant: Color,
        val background: Color,
        val onBackground: Color,
        val onBackgroundVariant: Color,
        val primary: Color,
        val onPrimary: Color,
        val collected: Color,
    ) {
        companion object {
            fun light() = Colors(
                surface = Reference.Colors.Grey.L50,
                onSurface = Reference.Colors.Grey.L800,
                onSurfaceVariant = Reference.Colors.Grey.L300,
                background = Reference.Colors.White,
                onBackground = Reference.Colors.Grey.L700,
                onBackgroundVariant = Reference.Colors.Grey.L300,
                primary = Reference.Colors.Grey.L100,
                onPrimary = Reference.Colors.Grey.L800,
                collected = Reference.Colors.Green.L400,
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

val LocalSystemColors = staticCompositionLocalOf { System.Colors.light() }
val LocalSystemSpacing = staticCompositionLocalOf { System.Spacing.mobile() }

object AppTheme {
    val Colors: System.Colors
        @Composable
        @ReadOnlyComposable
        get() = LocalSystemColors.current

    val Spacing: System.Spacing
        @Composable
        @ReadOnlyComposable
        get() = LocalSystemSpacing.current
}