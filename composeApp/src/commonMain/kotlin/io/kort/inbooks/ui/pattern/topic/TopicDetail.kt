package io.kort.inbooks.ui.pattern.topic

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import io.kort.inbooks.domain.model.topic.Topic
import io.kort.inbooks.ui.component.InteractionBox
import io.kort.inbooks.ui.pattern.book.BookCover
import io.kort.inbooks.ui.pattern.book.BookCoverDefaults
import io.kort.inbooks.ui.pattern.book.BookCoverLayoutStyle

@Composable
fun TopicDetail(
    topic: Topic,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    InteractionBox(
        modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = topic.title,
                fontSize = 16.sp,
                color = LocalContentColor.current,
            )
            BookThumbnails(topic)
        }
    }
}

@Composable
private fun BookThumbnails(
    topic: Topic
) {
    Box {
        remember(topic.books) { topic.books.takeLast(2).reversed() }.forEachIndexed { index, topicBook ->
            key(topicBook.book.id) {
                val rotate = remember(index) {
                    when (index) {
                        0 -> 4f
                        else -> -7f
                    }
                }

                val offset = remember(index) {
                    if (topic.books.size == 1) {
                        DpOffset.Zero
                    } else {
                        when (index) {
                            0 -> DpOffset(3.dp, (-4).dp)
                            else -> DpOffset((-3).dp, 2.dp)
                        }
                    }
                }

                BookCover(
                    modifier = Modifier
                        .alpha(1f)
                        .zIndex(index.toFloat())
                        .offset(offset.x, offset.y)
                        .graphicsLayer {
                            rotationZ = rotate
                        },
                    book = topicBook.book,
                    layout = BookCoverDefaults.layout(fillBy = BookCoverLayoutStyle.FillBy.Width.Small),
                )
            }
        }
    }
}
