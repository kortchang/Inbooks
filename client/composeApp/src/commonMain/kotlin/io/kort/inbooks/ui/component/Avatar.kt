package io.kort.inbooks.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import io.kort.inbooks.ui.resource.AvatarLarge
import io.kort.inbooks.ui.resource.Icons
import io.kort.inbooks.ui.token.system.System

@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    url: String?,
    onClick: () -> Unit = {},
) {
    Box(
        modifier
            .clickable(onClick = onClick)
            .size(40.dp)
            .clip(CircleShape)
            .background(System.colors.secondary)
    ) {
        if (url == null) {
            Icon(
                modifier = Modifier.matchParentSize(),
                imageVector = Icons.AvatarLarge,
                contentDescription = null,
                tint = System.colors.onSecondaryVariant,
            )
        } else {
            Image(
                modifier = Modifier.matchParentSize(),
                painter = rememberAsyncImagePainter(url),
                contentDescription = null,
            )
        }
    }
}

object AvatarDefaults {
    @Composable
    fun Modifier.redDot(): Modifier {
        val color = System.colors.warning
        val radius = 4.dp
        return drawWithContent {
            drawContent()
            drawCircle(
                color = color,
                radius = radius.toPx(),
                center = Offset(x = size.width - radius.toPx() - 2.dp.toPx(), y = radius.toPx() + 2.dp.toPx()),
            )
        }
    }
}