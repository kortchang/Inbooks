package io.kort.inbooks.ui.screen.book.detail.pattern

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.kort.inbooks.domain.model.book.Book
import io.kamel.core.Resource
import io.kamel.image.asyncPainterResource
import io.kort.inbooks.ui.foundation.Shadow
import io.kort.inbooks.ui.foundation.shadow
import io.kort.inbooks.ui.token.system.System

@Composable
fun SmallBookRowIfScrolled(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState,
    book: Book
) {
    val infoIsInvisible by remember(lazyListState) {
        derivedStateOf {
            lazyListState.layoutInfo.visibleItemsInfo.run {
                isNotEmpty() && none { it.key == "info" }
            }
        }
    }
    val offset = with(LocalDensity.current) { -24.dp.roundToPx() }
    AnimatedVisibility(
        visible = infoIsInvisible,
        enter = fadeIn() + slideInHorizontally() { offset },
        exit = fadeOut() + slideOutHorizontally { offset },
    ) {
        SmallBookRow(
            modifier = modifier,
            book = book
        )
    }
}

@Composable
private fun SmallBookRow(
    modifier: Modifier = Modifier,
    book: Book
) {
    Row(
        modifier = modifier
            .height(48.dp)
            .shadow(
                shadow = System.shadow.high,
                shape = CircleShape,
            )
            .background(Color.White, shape = CircleShape)
            .padding(horizontal = 48.dp / 2),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.clip(shape = RoundedCornerShape(4.dp))) {
            val coverResource = asyncPainterResource(book.coverUrl ?: "")
            when (coverResource) {
                is Resource.Failure -> Unit
                is Resource.Loading -> Spacer(Modifier.width(24.dp))
                is Resource.Success -> Image(
                    modifier = Modifier.width(24.dp),
                    painter = coverResource.value,
                    contentDescription = book.title,
                    contentScale = ContentScale.FillWidth
                )

            }
        }
        Text(
            modifier = Modifier.basicMarquee(),
            text = book.title,
            fontSize = 14.sp,
            color = Color(0xFF2A2A2A),
            fontWeight = FontWeight.Medium,
            maxLines = 1,
        )
    }
}
