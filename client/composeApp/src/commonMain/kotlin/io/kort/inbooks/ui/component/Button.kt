package io.kort.inbooks.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import io.kort.inbooks.ui.token.component.Component
import io.kort.inbooks.ui.token.reference.Reference

@Composable
fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.colorsOfBackground(),
    enabled: Boolean = true,
    start: @Composable (RowScope.() -> Unit)? = null,
    text: @Composable (RowScope.() -> Unit)? = null,
    end: @Composable (RowScope.() -> Unit)? = null,
) {
    val containerPaddingValues = PaddingValues(
        start = 12.dp,
        top = 12.dp,
        end = if (text != null && end == null) 16.dp else 12.dp,
        bottom = 12.dp
    )
    Row(
        modifier = modifier
            .alpha(if (enabled) 1f else Component.button.disabledAlpha)
            .clickable(onClick = onClick, enabled = enabled)
            .background(color = colors.backgroundColor, CircleShape)
            .padding(containerPaddingValues),
        horizontalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CompositionLocalProvider(
            LocalContentColor provides colors.onBackgroundColor,
            LocalTextStyle provides ButtonDefaults.textStyle().copy(color = colors.onBackgroundColor)
        ) {
            start?.invoke(this)
            text?.invoke(this)
            end?.invoke(this)
        }
    }
}

object ButtonDefaults {
    @Composable
    fun colorsOfBackground() = ButtonColors(
        backgroundColor = Component.button.backgroundBackground,
        onBackgroundColor = Component.button.backgroundOnBackground
    )

    @Composable
    fun colorsOfPrimary() = ButtonColors(
        backgroundColor = Component.button.primaryBackground,
        onBackgroundColor = Component.button.primaryOnBackground
    )

    @Composable
    fun colorsOfSecondary() = ButtonColors(
        backgroundColor = Component.button.secondaryBackground,
        onBackgroundColor = Component.button.secondaryOnBackground
    )

    @Composable
    fun colorsOfWarningText() = ButtonColors(
        backgroundColor = Reference.Colors.Transparent,
        onBackgroundColor = Component.button.warningOnBackground,
    )

    @Composable
    fun textStyle(): TextStyle = Component.button.typography
}

data class ButtonColors(
    val backgroundColor: Color,
    val onBackgroundColor: Color
)