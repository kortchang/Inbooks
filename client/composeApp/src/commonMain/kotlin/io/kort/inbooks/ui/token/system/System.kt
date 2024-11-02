package io.kort.inbooks.ui.token.system

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.kort.inbooks.ui.token.reference.Reference

data class System(
    val colors: Colors,
    val spacing: Spacing,
    val shadow: Shadow
) {
    companion object {
        fun default() = System(
            colors = Colors.light(),
            spacing = Spacing.mobile(),
            shadow = Shadow.light()
        )

        val colors: Colors
            @Composable
            get() = LocalSystem.current.colors

        val spacing: Spacing
            @Composable
            get() = LocalSystem.current.spacing

        val shadow: Shadow
            @Composable
            get() = LocalSystem.current.shadow
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
        val outlineAlpha: Float,
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
                outlineAlpha = 0.15f,
                warning = Reference.Colors.Red.L400,
            )

            fun dark() = Colors(
                surface = Reference.Colors.Grey.L700,
                onSurface = Reference.Colors.Grey.L50,
                onSurfaceVariant = Reference.Colors.Grey.L200,
                background = Reference.Colors.Grey.L600,
                onBackground = Reference.Colors.Grey.L50,
                onBackgroundVariant = Reference.Colors.Grey.L200,
                primary = Reference.Colors.Grey.L50,
                onPrimary = Reference.Colors.Grey.L700,
                secondary = Reference.Colors.Grey.L200,
                onSecondary = Reference.Colors.Grey.L800,
                onSecondaryVariant = Reference.Colors.Grey.L400,
                collected = Reference.Colors.Green.L300,
                outlineAlpha = 0.15f,
                warning = Reference.Colors.Red.L400,
            )
        }
    }

    class Spacing(
        val pagePadding: PaddingValues
    ) {
        companion object {
            fun mobile() = Spacing(
                pagePadding = PaddingValues(vertical = 16.dp, horizontal = 32.dp)
            )
        }
    }

    class Shadow(
        val low: io.kort.inbooks.ui.foundation.Shadow,
        val medium: io.kort.inbooks.ui.foundation.Shadow,
        val high: io.kort.inbooks.ui.foundation.Shadow,
    ) {
        companion object {
            fun light() =
                Shadow(
                    low = io.kort.inbooks.ui.foundation.Shadow.Drop(
                        color = Reference.Colors.Black.copy(alpha = 0.15f),
                        blur = 10.dp,
                        offsetX = (-4).dp,
                        offsetY = 4.dp
                    ),
                    medium = io.kort.inbooks.ui.foundation.Shadow.Drop(
                        color = Reference.Colors.Black.copy(alpha = 0.08f),
                        blur = 20.dp,
                        offsetY = 4.dp,
                    ),
                    high = io.kort.inbooks.ui.foundation.Shadow.Drop(
                        Reference.Colors.Black.copy(alpha = 0.04f),
                        blur = 20.dp,
                        offsetY = 5.dp
                    )
                )

            fun dark() = Shadow(
                low = NoShadow,
                medium = NoShadow,
                high = NoShadow
            )

            private val NoShadow = io.kort.inbooks.ui.foundation.Shadow.Drop(color = Color.Transparent, blur = 0.dp)
        }
    }
}

val LocalSystem = compositionLocalOf { System.default() }