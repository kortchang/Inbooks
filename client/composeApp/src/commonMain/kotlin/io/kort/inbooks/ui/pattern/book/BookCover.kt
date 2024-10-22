package io.kort.inbooks.ui.pattern.book

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import io.kort.inbooks.domain.model.book.Book
import io.kort.inbooks.ui.foundation.SharedScene
import io.kort.inbooks.ui.foundation.shadow
import io.kort.inbooks.ui.foundation.sharedElement
import io.kort.inbooks.ui.token.reference.Reference
import io.kort.inbooks.ui.token.system.System

@Composable
fun BookCover(
    book: Book,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    layout: BookCoverLayoutStyle = BookCoverDefaults.layout(),
    share: BookCoverShareStyle = BookCoverDefaults.share(),
) {
    val imageState by rememberAsyncImagePainter(book.coverUrl).state.collectAsState()
    val clickModifier = modifier
        .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)

    val coverNotExistModifier = Modifier
        .then(
            when (layout.fillBy) {
                is BookCoverLayoutStyle.FillBy.Width ->
                    Modifier
                        .width(layout.fillBy.value)
                        .aspectRatio(2 / 3f)

                is BookCoverLayoutStyle.FillBy.Height ->
                    Modifier
                        .height(layout.fillBy.value)
                        .aspectRatio(2 / 3f, matchHeightConstraintsFirst = true)
            }
        )

    when (val state = imageState) {
        is AsyncImagePainter.State.Error -> {
            Box(
                modifier = Modifier
                    .then(clickModifier)
                    .then(coverNotExistModifier)
                    .background(Color(0xFFA0A0A0))
            ) {
                Text(
                    modifier = Modifier.align(Alignment.BottomStart),
                    text = book.title,
                    fontSize = 14.sp,
                    maxLines = 2,
                    color = Reference.Colors.White
                )
            }
        }

        AsyncImagePainter.State.Empty, is AsyncImagePainter.State.Loading -> {
            Spacer(
                clickModifier then coverNotExistModifier
            )
        }

        is AsyncImagePainter.State.Success -> {
            Image(
                modifier = Modifier
                    .then(
                        when (layout.fillBy) {
                            is BookCoverLayoutStyle.FillBy.Width -> Modifier.width(layout.fillBy.value)
                            is BookCoverLayoutStyle.FillBy.Height -> Modifier.height(layout.fillBy.value)
                        }
                    )
                    .then(
                        Modifier.sharedElement(
                            scenes = share.scenes,
                            key = "book_${book.id}",
                            zIndexInOverlay = share.zIndex?.toFloat() ?: 0f
                        )
                    )
                    .then(clickModifier)
                    .dropShadow()
                    .edgeShadow(),
                painter = state.painter,
                contentDescription = book.title,
                contentScale = when (layout.fillBy) {
                    is BookCoverLayoutStyle.FillBy.Width -> ContentScale.FillWidth
                    is BookCoverLayoutStyle.FillBy.Height -> ContentScale.FillHeight
                }
            )
        }
    }
}

@Composable
private fun Modifier.dropShadow(): Modifier {
    return shadow(
        shadow = System.shadow.low,
        shape = RoundedCornerShape(3.dp)
    )
}

@Composable
private fun Modifier.edgeShadow() =
    drawWithContent {
        drawContent()
        val scale = size.width.toDp() / BookCoverLayoutStyle.FillBy.Width.Medium.value
        scale(scaleX = scale, scaleY = 1f, pivot = Offset.Zero) {
            drawRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF000000),
                        Color(0xFF818181),
                    ),
                    endX = 5.dp.toPx()
                ),
                alpha = 0.2f,
                size = Size(5.dp.toPx(), size.height),
            )
            drawRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFB7B7B7),
                        Color(0xFF000000),
                    ),
                    startX = 5.dp.toPx(),
                    endX = 8.dp.toPx(),
                ),
                topLeft = Offset(5.dp.toPx(), 0f),
                alpha = 0.2f,
                size = Size(3.dp.toPx(), size.height)
            )
        }
    }

data class BookCoverLayoutStyle(
    val fillBy: FillBy
) {
    sealed interface FillBy {
        sealed class Width(val value: Dp) : FillBy {
            data object Small : Width(24.dp)
            data object Medium : Width(75.dp)
            data object Large : Width(100.dp)
        }

        sealed class Height(val value: Dp) : FillBy {
            data object Small : Height(45.dp)
            data object Medium : Height(105.dp)
            data object Large : Height(155.dp)
        }
    }
}

data class BookCoverShareStyle(
    val scenes: List<SharedScene>,
    val zIndex: Int?
)

object BookCoverDefaults {
    fun layout(
        fillBy: BookCoverLayoutStyle.FillBy = BookCoverLayoutStyle.FillBy.Width.Medium,
    ) = BookCoverLayoutStyle(
        fillBy = fillBy
    )

    fun share(
        scenes: List<SharedScene> = emptyList(),
        zIndex: Int? = null,
    ) = BookCoverShareStyle(
        zIndex = zIndex,
        scenes = scenes.toList()
    )
}