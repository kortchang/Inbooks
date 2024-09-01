package ui.component

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import domain.book.Book
import io.kamel.core.Resource
import io.kamel.image.asyncPainterResource
import ui.foundation.Shadow
import ui.foundation.shadow

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun BookCover(
    book: Book,
    onClick: (() -> Unit)? = null,
    sharedTransitionScope: SharedTransitionScope?,
    animatedContentScope: AnimatedContentScope?,
    style: BookCoverStyle = BookCoverDefaults.layout(),
) {
    Box(
        Modifier
            .sharedElement(book.id, sharedTransitionScope, animatedContentScope)
            .dropShadow(book.coverUrl ?: "", style.shadowColor)
            .edgeShadow()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
    ) {
        val coverResource = asyncPainterResource(book.coverUrl ?: "")
        when (coverResource) {
            is Resource.Failure -> Unit
            is Resource.Loading -> Spacer(
                Modifier
                    .background(Color(0xFFA0A0A0))
                    .width(style.width.value)
                    .aspectRatio(2 / 3f)
            )

            is Resource.Success -> {
                Image(
                    modifier = Modifier.width(style.width.value),
                    painter = coverResource.value,
                    contentDescription = book.title,
                    contentScale = ContentScale.FillWidth
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun Modifier.sharedElement(
    bookId: String,
    sharedTransitionScope: SharedTransitionScope?,
    animatedContentScope: AnimatedContentScope?,
) = then(
    if (sharedTransitionScope != null && animatedContentScope != null) {
        with(sharedTransitionScope) {
            Modifier.sharedElement(
                state = sharedTransitionScope.rememberSharedContentState(
                    "book-${bookId}"
                ),
                animatedVisibilityScope = animatedContentScope,
            )
        }
    } else {
        Modifier
    }
)

@Composable
private fun Modifier.dropShadow(
    bookCoverUrl: String,
    shadowColor: BookCoverStyle.ShadowColor
): Modifier {
    return when (shadowColor) {
        BookCoverStyle.ShadowColor.Environment -> {
            shadow(
                shadow = Shadow.Drop(
                    color = Color.Black.copy(alpha = 0.15f),
                    blur = 20.dp,
                    offsetX = (-4).dp,
                    offsetY = 4.dp
                ),
                shape = RoundedCornerShape(3.dp)
            )
        }

        BookCoverStyle.ShadowColor.Cover -> {
            val color = rememberBookCoverColor(bookCoverUrl).color
            shadow(
                shadow = Shadow.Drop(
                    color = color.copy(alpha = 0.08f),
                    blur = 100.dp,
                    spread = 50.dp,
                ),
                shape = CircleShape,
                clip = false,
            )
        }
    }
}


@Composable
private fun Modifier.edgeShadow() =
    drawWithContent {
        drawContent()
        drawRect(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color(0xFF000000),
                    Color(0xFF818181),
                ),
                endX = 5.dp.toPx()
            ),
            alpha = 0.2f,
            size = Size(5.dp.toPx(), size.height)
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

data class BookCoverStyle(
    val width: Width,
    val shadowColor: ShadowColor,
) {
    enum class Width(val value: Dp) {
        Small(72.dp), Medium(103.dp)
    }

    enum class ShadowColor {
        Environment, Cover
    }
}

object BookCoverDefaults {
    fun layout(
        width: BookCoverStyle.Width = BookCoverStyle.Width.Small,
        shadowColor: BookCoverStyle.ShadowColor = BookCoverStyle.ShadowColor.Environment,
    ) = BookCoverStyle(
        width = width,
        shadowColor = shadowColor
    )
}