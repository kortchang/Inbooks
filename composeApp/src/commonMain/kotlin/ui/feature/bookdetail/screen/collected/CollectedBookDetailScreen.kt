@file:OptIn(ExperimentalSharedTransitionApi::class)

package ui.feature.bookdetail.screen.collected

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.unit.dp
import app.di.getViewModel
import book.composeapp.generated.resources.Res
import book.composeapp.generated.resources.icon_nav_arrow_left
import domain.book.CollectedBook
import org.jetbrains.compose.resources.painterResource
import org.koin.core.parameter.parametersOf
import ui.component.BookCoverDefaults
import ui.component.BookCoverStyle
import ui.component.BookWithInformation
import ui.component.FloatButtonBox
import ui.feature.bookdetail.pattern.SmallBookRowIfScrolled
import ui.feature.bookdetail.pattern.bookInformation
import ui.feature.bookdetail.pattern.detailTopAppBar
import ui.feature.bookdetail.pattern.readingEvents
import ui.feature.bookdetail.pattern.reason
import ui.foundation.Shadow
import ui.foundation.shadow
import ui.token.AppTheme

@Composable
fun CollectedBookDetailScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    bookId: String,
    back: () -> Unit
) {
    Surface {
        val viewModel = getViewModel<CollectedBookDetailViewModel> { parametersOf(bookId) }
        val uiState by viewModel.uiState.collectAsState()
        val bookOrNull = uiState.book
        val contentLazyListState = rememberLazyListState()
        bookOrNull?.let { book ->
            Content(
                modifier = Modifier.fillMaxHeight(),
                book = book,
                lazyListState = contentLazyListState,
                back = back,
                onReadingReasonChange = { reason ->
                    uiState.intentTo(CollectedBookDetailUiIntent.UpdateReadingReason(reason))
                },
                onReadingEventAdd = { page ->
                    uiState.intentTo(CollectedBookDetailUiIntent.AddReadingEvent(page))
                },
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Content(
    modifier: Modifier = Modifier,
    book: CollectedBook,
    lazyListState: LazyListState,
    back: () -> Unit,
    onReadingEventAdd: (Int) -> Unit = {},
    onReadingReasonChange: (String) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
) {
    LazyColumn(
        modifier = modifier,
        state = lazyListState,
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(36.dp)
    ) {
        detailTopAppBar(lazyListState, book.book, back)

        val scope = CollectedBookDetailContentScope(this)
        item(key = "info") {
            BookWithInformation(
                modifier = Modifier.padding(horizontal = 36.dp),
                book = book.book,
                padding = PaddingValues(),
                bookCoverStyle = BookCoverDefaults.layout(
                    width = BookCoverStyle.Width.Medium,
                    shadowColor = BookCoverStyle.ShadowColor.Cover,
                ),
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope,
            )
        }

        with(scope) {
            reason(value = book.readingReason, onValueChange = onReadingReasonChange)
            readingEvents(book = book, onReadingEventAdd = onReadingEventAdd)
            bookInformation(book = book.book)
        }
    }
}

data class CollectedBookDetailContentScope(private val scope: LazyListScope) :
    LazyListScope by scope