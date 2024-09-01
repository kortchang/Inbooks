@file:OptIn(ExperimentalSharedTransitionApi::class)

package ui.feature.bookdetail.screen.searched

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.di.getViewModel
import book.composeapp.generated.resources.Res
import book.composeapp.generated.resources.icon_collage_frame
import book.composeapp.generated.resources.icon_nav_arrow_left
import book.composeapp.generated.resources.searched_book_detail_collect
import domain.book.SearchedBook
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.core.parameter.parametersOf
import ui.component.BookWithInformation
import ui.component.FloatButtonBox
import ui.feature.bookdetail.pattern.SmallBookRowIfScrolled
import ui.feature.bookdetail.pattern.bookInformation
import ui.feature.bookdetail.pattern.detailTopAppBar

@Composable
fun SearchedBookDetailScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    bookIdBySource: String,
    back: () -> Unit,
    navigateToCollectedBookDetail: (bookId: String) -> Unit,
) {
    Surface {
        val viewModel = getViewModel<SearchedBookDetailViewModel> { parametersOf(bookIdBySource) }

        LaunchedEffect(Unit) {
            viewModel.uiEvent.collectLatest { event ->
                when (event) {
                    is SearchedBookDetailUiEvent.Collected -> {
                        navigateToCollectedBookDetail(event.collectedBookId)
                    }
                }
            }
        }

        val uiState by viewModel.uiState.collectAsState()
        val bookOrNull = uiState.book
        val contentLazyListState = rememberLazyListState()
        Column(
            Modifier
                .fillMaxSize()
        ) {
            bookOrNull?.let { book ->
                Content(
                    modifier = Modifier.weight(1f),
                    book = book,
                    back = back,
                    lazyListState = contentLazyListState,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedContentScope = animatedContentScope,
                )
                Spacer(Modifier.height(16.dp))
                CollectButton(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = { uiState.intentTo(SearchedBookDetailUiIntent.Collect) }
                )
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    book: SearchedBook,
    back: () -> Unit,
    lazyListState: LazyListState,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
) {
    LazyColumn(
        modifier = modifier,
        state = lazyListState,
        contentPadding = PaddingValues(vertical = 24.dp)
    ) {
        detailTopAppBar(lazyListState, book.book, back)

        item(key = "info") {
            BookWithInformation(
                modifier = Modifier.padding(horizontal = 20.dp),
                book = book.book,
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope,
            )
        }

        item {
            Spacer(Modifier.height(36.dp))
        }

        bookInformation(book.book)
    }
}

@Composable
private fun CollectButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    FloatButtonBox(
        modifier = modifier.clickable(onClick = onClick),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(Res.drawable.icon_collage_frame),
                contentDescription = null
            )
            Text(
                text = stringResource(Res.string.searched_book_detail_collect),
                fontSize = 16.sp,
                color = Color(0xFF6F6F6F),
                fontWeight = FontWeight.W600,
            )
        }
    }
}