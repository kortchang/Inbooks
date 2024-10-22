package io.kort.inbooks.ui.token.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalWithComputedDefaultOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

data class Component(
    val button: Button,
) {
    data class Button(
        val backgroundBackground: Color,
        val backgroundOnBackground: Color,
        val primaryBackground: Color,
        val primaryOnBackground: Color,
        val secondaryBackground: Color,
        val secondaryOnBackground: Color,
        val warningOnBackground: Color,
        val disabledAlpha: Float,
        val typography: TextStyle
    )

    companion object {
        fun default(system: io.kort.inbooks.ui.token.system.System): Component {
            return Component(
                button = Button(
                    backgroundBackground = system.colors.background,
                    backgroundOnBackground = system.colors.onBackground,
                    primaryBackground = system.colors.primary,
                    primaryOnBackground = system.colors.onPrimary,
                    secondaryBackground = system.colors.secondary,
                    secondaryOnBackground = system.colors.onSecondary,
                    warningOnBackground = system.colors.warning,
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