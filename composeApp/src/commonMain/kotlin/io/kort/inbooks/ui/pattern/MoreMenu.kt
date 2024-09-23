package io.kort.inbooks.ui.pattern

import androidx.compose.foundation.BasicTooltipBox
import androidx.compose.foundation.BasicTooltipState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupPositionProvider
import io.kort.inbooks.ui.token.component.Component

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MoreMenuBox(
    state: BasicTooltipState,
    backgroundColor: Color = Component.button.secondaryBackground,
    menuContent: @Composable() (ColumnScope.() -> Unit),
    content: @Composable () -> Unit,
) {
    BasicTooltipBox(
        positionProvider = rememberRichTooltipPositionProvider(12.dp),
        state = state,
        tooltip = {
            Column(
                modifier = Modifier.background(backgroundColor, shape = RoundedCornerShape(16.dp)),
                content = menuContent
            )
        },
        content = content
    )
}

@Composable
fun MoreMenuItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    color: Color = LocalContentColor.current,
) {
    Row(
        modifier = modifier.clickable(onClick = onClick).padding(16.dp).padding(end = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = color)
        Text(text = text, color = color, fontSize = 16.sp, fontWeight = FontWeight.W600)
    }
}

@Composable
fun rememberRichTooltipPositionProvider(
    spacingBetweenTooltipAndAnchor: Dp,
): PopupPositionProvider {
    val tooltipAnchorSpacing =
        with(LocalDensity.current) { spacingBetweenTooltipAndAnchor.roundToPx() }
    return remember(tooltipAnchorSpacing) {
        object : PopupPositionProvider {
            override fun calculatePosition(
                anchorBounds: IntRect,
                windowSize: IntSize,
                layoutDirection: LayoutDirection,
                popupContentSize: IntSize
            ): IntOffset {
                var x = anchorBounds.left
                // Try to shift it to the left of the anchor
                // if the tooltip would collide with the right side of the screen
                if (x + popupContentSize.width > windowSize.width) {
                    x = anchorBounds.right - popupContentSize.width
                    // Center if it'll also collide with the left side of the screen
                    if (x < 0)
                        x =
                            anchorBounds.left +
                                    (anchorBounds.width - popupContentSize.width) / 2
                }

                // Tooltip prefers to be below the anchor,
                // but if this causes the tooltip to overlap with the anchor
                // then we place it above the anchor
                var y = anchorBounds.bottom + tooltipAnchorSpacing
                if (y + popupContentSize.height > windowSize.height) y = anchorBounds.top - popupContentSize.height - tooltipAnchorSpacing
                return IntOffset(x, y)
            }
        }
    }
}
