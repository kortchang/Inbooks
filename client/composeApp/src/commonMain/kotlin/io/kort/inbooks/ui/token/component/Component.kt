package io.kort.inbooks.ui.token.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalWithComputedDefaultOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Component(
    val button: Button,
) {
    data class Button(
        val backgroundButtonBackground: Color,
        val backgroundButtonOnBackground: Color,
        val primaryButtonBackground: Color,
        val primaryButtonOnBackground: Color,
        val secondaryButtonBackground: Color,
        val secondaryButtonOnBackground: Color,
        val warningTextButtonOnBackground: Color,
        val outlineButtonOnBackground: Color,
        val outlineButtonOutlineAlpha: Float,
        val outlineButtonStrokeWidth: Dp,
        val disabledAlpha: Float,
        val typography: TextStyle
    )

    companion object {
        fun default(system: io.kort.inbooks.ui.token.system.System): Component {
            return Component(
                button = Button(
                    backgroundButtonBackground = system.colors.background,
                    backgroundButtonOnBackground = system.colors.onBackground,
                    primaryButtonBackground = system.colors.primary,
                    primaryButtonOnBackground = system.colors.onPrimary,
                    secondaryButtonBackground = system.colors.secondary,
                    secondaryButtonOnBackground = system.colors.onSecondary,
                    warningTextButtonOnBackground = system.colors.warning,
                    outlineButtonOnBackground = system.colors.onBackground,
                    outlineButtonOutlineAlpha = system.colors.outlineAlpha,
                    outlineButtonStrokeWidth = 1.dp,
                    disabledAlpha = 0.5f,
                    typography = TextStyle(
                        fontSize = 16.sp,
                    )
                )
            )
        }

        val button: Button
            @ReadOnlyComposable
            @Composable
            get() = LocalComponent.current.button
    }
}

val LocalComponent = compositionLocalWithComputedDefaultOf {
    Component.default(io.kort.inbooks.ui.token.system.LocalSystem.currentValue)
}