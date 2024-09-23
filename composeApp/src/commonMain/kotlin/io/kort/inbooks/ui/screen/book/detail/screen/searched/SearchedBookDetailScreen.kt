package io.kort.inbooks.ui.screen.book.detail.screen.searched

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.kort.inbooks.app.di.getViewModel
import io.kort.inbooks.domain.model.book.Book
import io.kort.inbooks.domain.model.book.SearchedBook
import io.kort.inbooks.ui.component.Button
import io.kort.inbooks.ui.component.ButtonDefaults
import io.kort.inbooks.ui.component.PageScope
import io.kort.inbooks.ui.component.TopAppBar
import io.kort.inbooks.ui.foundation.SharedScene
import io.kort.inbooks.ui.foundation.plus
import io.kort.inbooks.ui.pattern.book.BookCover
import io.kort.inbooks.ui.pattern.book.BookCoverDefaults
import io.kort.inbooks.ui.pattern.book.BookCoverLayoutStyle
import io.kort.inbooks.ui.pattern.book.BookDetail
import io.kort.inbooks.ui.pattern.book.BookTitle
import io.kort.inbooks.ui.resource.Icons
import io.kort.inbooks.ui.resource.NavArrowLeft
import io.kort.inbooks.ui.resource.PlusCircle
import io.kort.inbooks.ui.resource.Res
import io.kort.inbooks.ui.resource.searched_book_detail_collect
import io.kort.inbooks.ui.screen.book.detail.pattern.BookInformation
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.stringResource
import org.koin.core.parameter.parametersOf

@Composable
fun PageScope.SearchedBookDetailScreen(
    externalIds: List<Book.ExternalId>,
    back: () -> Unit,
    navigateToCollectedBookDetail: (bookId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = getViewModel<SearchedBookDetailViewModel> { parametersOf(externalIds) }

    val updatedBack by rememberUpdatedState(back)
    val updatedNavigateToCollectedBookDetail by rememberUpdatedState(navigateToCollectedBookDetail)
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                SearchedBookDetailUiEvent.BookNotExist -> updatedBack()
                is SearchedBookDetailUiEvent.Collected -> {
                    updatedNavigateToCollectedBookDetail(event.collectedBookId)
                }
            }
        }
    }

    val uiState by viewModel.uiState.collectAsState()
    val bookOrNull = uiState.book
    val contentLazyListState = rememberLazyListState()
    val bottomWindowInsets = windowInsets.only(WindowInsetsSides.Bottom)
    val excludeBottomWindowInsets = windowInsets.exclude(bottomWindowInsets)
    Column(modifier.fillMaxSize().windowInsetsPadding(excludeBottomWindowInsets)) {
        SearchedBookDetailTopAppBar(
            onBack = back,
            onCollect = { uiState.intentTo(SearchedBookDetailUiIntent.Collect) }
        )
        bookOrNull?.let { book ->
            Content(
                modifier = Modifier.weight(1f),
                book = book,
                lazyListState = contentLazyListState,
                contentPadding = bottomWindowInsets.asPaddingValues(),
            )
        }
    }
}

@Composable
private fun SearchedBookDetailTopAppBar(
    onBack: () -> Unit,
    onCollect: () -> Unit,
) {
    TopAppBar(
        start = {
            Button(
                start = {
                    Icon(
                        imageVector = Icons.NavArrowLeft,
                        contentDescription = null,
                    )
                },
                onClick = onBack
            )
        },
        center = {
            Spacer(Modifier.weight(1f))
        },
        end = {
            Button(
                start = {
                    Icon(
                        imageVector = Icons.PlusCircle,
                        contentDescription = null,
                    )
                },
                text = {
                    Text(text = stringResource(Res.string.searched_book_detail_collect))
                },
                onClick = onCollect,
                colors = ButtonDefaults.colorsOfSecondary(),
            )
        }
    )
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    book: SearchedBook,
    lazyListState: LazyListState,
    contentPadding: PaddingValues,
) {
    LazyColumn(
        modifier = modifier,
        state = lazyListState,
        contentPadding = contentPadding + PaddingValues(top = 48.dp),
    ) {
        item(key = "info") {
            Book(book = book)
        }

        item {
            Spacer(Modifier.height(32.dp))
        }

        item { BookInformation(book.book) }
    }
}

@Composable
private fun Book(
    book: SearchedBook
) {
    BookDetail(
        cover = {
            BookCover(
                book = book.book,
                layout = BookCoverDefaults.layout(fillBy = BookCoverLayoutStyle.FillBy.Width.Large),
                share = BookCoverDefaults.share(SharedScene.ForBookDetail)
            )
        },
        title = {
            BookTitle(
                book = book.book,
            )
        }
    )
}