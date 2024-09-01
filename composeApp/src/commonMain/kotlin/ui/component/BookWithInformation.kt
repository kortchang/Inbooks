@file:OptIn(ExperimentalSharedTransitionApi::class)

package ui.component

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.touchlab.kermit.Logger
import com.kmpalette.color
import com.kmpalette.titleTextColor
import domain.book.Book
import kotlinx.datetime.Instant

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun BookWithInformation(
    modifier: Modifier = Modifier,
    book: Book,
    collectedAt: Instant? = null,
    onlyCover: Boolean = false,
    onClick: (() -> Unit)? = null,
    bookCoverStyle: BookCoverStyle = BookCoverDefaults.layout(),
    padding: PaddingValues = PaddingValues(16.dp),
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedContentScope: AnimatedContentScope? = null,
) {
    Row(
        modifier = modifier
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(padding),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        BookCover(
            book = book,
            sharedTransitionScope = sharedTransitionScope,
            animatedContentScope = animatedContentScope,
            style = bookCoverStyle
        )

        val color = rememberBookCoverColor(book.coverUrl ?: "")
        if (onlyCover.not()) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = book.title,
                    fontSize = 16.sp,
                    color = color.color,
                    fontWeight = FontWeight.W600,
                    overflow = TextOverflow.Ellipsis,
                )
                if (book.authors != null) {
                    LaunchedEffect(color.result) {
                        Logger.d("[Kort Debug]") { "swatches: ${color.result?.paletteOrNull?.swatches}" }
                    }
                    Text(
                        text = book.authors.joinToString(" "),
                        fontSize = 14.sp,
                        color = color.result?.paletteOrNull?.vibrantSwatch?.color ?: Color.Gray,
                    )
                }
            }
        }
    }
}

