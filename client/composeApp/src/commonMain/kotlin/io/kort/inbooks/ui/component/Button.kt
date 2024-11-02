package io.kort.inbooks.ui.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.kort.inbooks.ui.foundation.sweepGradient
import io.kort.inbooks.ui.token.component.Component
import io.kort.inbooks.ui.token.reference.Reference
import io.kort.inbooks.ui.token.system.System

@Composable
fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.backgroundButtonColors(),
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
            .border(Component.button.outlineButtonStrokeWidth, colors.outlineColor, ButtonDefaults.Shape)
            .background(color = colors.backgroundColor, ButtonDefaults.Shape)
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
    fun backgroundButtonColors() = ButtonColors(
        backgroundColor = Component.button.backgroundButtonBackground,
        onBackgroundColor = Component.button.backgroundButtonOnBackground,
    )

    @Composable
    fun primaryButtonColors() = ButtonColors(
        backgroundColor = Component.button.primaryButtonBackground,
        onBackgroundColor = Component.button.primaryButtonOnBackground,
    )

    @Composable
    fun secondaryButtonColors() = ButtonColors(
        backgroundColor = Component.button.secondaryButtonBackground,
        onBackgroundColor = Component.button.secondaryButtonOnBackground,
    )

    @Composable
    fun warningTextButtonColors() = ButtonColors(
        backgroundColor = Reference.Colors.Transparent,
        onBackgroundColor = Component.button.warningTextButtonOnBackground,
    )

    @Composable
    fun outlineButtonColors() = ButtonColors(
        backgroundColor = Color.Transparent,
        onBackgroundColor = Component.button.backgroundButtonOnBackground,
        outlineColor = Component.button.backgroundButtonOnBackground.copy(alpha = Component.button.outlineButtonOutlineAlpha)
    )

    @Composable
    fun textStyle(): TextStyle = Component.button.typography

    @Composable
    fun Modifier.loadingIndicator(
        color: Color,
        strokeWidth: Dp = 1.5.dp
    ): Modifier {
        val radiusTransition = rememberInfiniteTransition()
        val radius by radiusTransition.animateFloat(
            -45f,
            -405f,
            infiniteRepeatable(animation = tween(durationMillis = 2000, easing = LinearEasing))
        )

        return drawWithCache {
            val outline = Shape.createOutline(size, layoutDirection, this)
            val stroke = Stroke(strokeWidth.toPx())
            onDrawWithContent {
                drawContent()
                drawOutline(
                    outline = outline,
                    brush = Brush.sweepGradient(
                        0f to color,
                        0.2f to Color.Transparent,
                        1.0f to Color.Transparent,
                        degrees = radius,
                    ),
                    style = stroke
                )
            }
        }.padding(strokeWidth / 2)
    }

    val Shape = CircleShape
}

data class ButtonColors(
    val backgroundColor: Color,
    val onBackgroundColor: Color,
    val outlineColor: Color = Color.Transparent
)