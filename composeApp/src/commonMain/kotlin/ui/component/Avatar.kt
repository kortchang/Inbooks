package ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    url: String,
    onClick: () -> Unit = {},
) {
    KamelImage(
        modifier = modifier
            .clip(CircleShape)
            .size(48.dp)
            .clickable(onClick = onClick),
        resource = asyncPainterResource(url),
        contentDescription = null,
    )
}