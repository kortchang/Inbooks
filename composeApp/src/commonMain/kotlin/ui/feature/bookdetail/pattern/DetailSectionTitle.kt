package ui.feature.bookdetail.pattern

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DetailSection(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Column(modifier
        .drawWithCache {
            val horizontalPadding = 24.dp.toPx()
            val strokeWidth = 1.dp.toPx()
            onDrawBehind {
                drawLine(
                    color = Color(0xFFE9E9E9),
                    start = Offset(horizontalPadding, size.height - strokeWidth),
                    end = Offset(size.width - horizontalPadding, size.height - strokeWidth),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round,
                )
            }
        }
        .padding(horizontal = 36.dp)
    ) {
        title()
        Spacer(Modifier.height(24.dp))
        content()
        Spacer(Modifier.height(36.dp))
    }
}

@Composable
fun DetailSectionTitle(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    title: String,
    end: @Composable () -> Unit = {}
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CompositionLocalProvider(
            LocalContentColor provides Color(0xFF616161)
        ) {
            icon()
        }
        Text(
            modifier = Modifier.weight(1f),
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.W600,
            maxLines = 1,
            color = Color(0xFF4A4A4A),
        )
        CompositionLocalProvider(
            LocalContentColor provides Color(0xFFA2A2A2)
        ) {
            end()
        }
    }
}

@Composable
fun DetailSectionTitle(
    modifier: Modifier = Modifier,
    icon: Painter,
    title: String,
    end: @Composable () -> Unit = {}
) {
    DetailSectionTitle(
        modifier = modifier,
        icon = {
            Icon(
                painter = icon,
                contentDescription = title,
            )
        },
        title = title,
        end = end
    )
}