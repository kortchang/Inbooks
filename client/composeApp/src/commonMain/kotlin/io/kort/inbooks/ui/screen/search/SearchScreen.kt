package io.kort.inbooks.ui.screen.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.kort.inbooks.ui.resource.Res
import io.kort.inbooks.ui.resource.book_collected
import io.kort.inbooks.ui.resource.powered_by_google
import io.kort.inbooks.ui.resource.search_book_placeholder
import io.kort.inbooks.app.di.getViewModel
import io.kort.inbooks.domain.model.book.BookId
import io.kort.inbooks.domain.model.book.CollectedBook
import io.kort.inbooks.domain.model.book.SearchedBook
import io.kort.inbooks.ui.component.InteractionBox
import io.kort.inbooks.ui.component.InteractionBoxDefaults
import io.kort.inbooks.ui.component.PageScope
import io.kort.inbooks.ui.foundation.plus
import io.kort.inbooks.ui.pattern.SearchField
import io.kort.inbooks.ui.pattern.book.BookCover
import io.kort.inbooks.ui.pattern.book.BookCoverDefaults
import io.kort.inbooks.ui.pattern.book.BookCoverLayoutStyle
import io.kort.inbooks.ui.pattern.book.BookDetail
import io.kort.inbooks.ui.pattern.book.BookTitle
import io.kort.inbooks.ui.resource.CheckSmall
import io.kort.inbooks.ui.resource.Icons
import io.kort.inbooks.ui.resource.Language
import io.kort.inbooks.ui.resource.new_tag
import io.kort.inbooks.ui.resource.search_book_add_by_link_books_tw_title
import io.kort.inbooks.ui.resource.search_book_error_google_too_many_requests
import io.kort.inbooks.ui.screen.search.addbylink.AddByLinkSheet
import io.kort.inbooks.ui.token.system.System
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import org.jetbrains.compose.resources.stringResource

@Composable
fun PageScope.SearchScreen(
    navigateToSearchedDetail: (SearchedBook) -> Unit,
    navigateToCollectedDetail: (BookId) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = getViewModel<SearchViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val bottomWindowInsets = windowInsets.only(WindowInsetsSides.Bottom)
    val excludeBottomWindowInsets = windowInsets.exclude(bottomWindowInsets)
    Box(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(excludeBottomWindowInsets),
    ) {
        Column(Modifier.matchParentSize()) {
            SearchTopAppBar(
                isSearching = uiState.isSearching,
                initialQuery = uiState.query,
                onQueryChange = {
                    uiState.intentTo(SearchUiIntent.Search(it))
                }
            )
            if (uiState.query.isEmpty()) {
                Dashboard(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    navigateToCollectedBookDetail = navigateToCollectedDetail,
                )
            } else {
                SearchedBooksList(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    books = uiState.books,
                    onClickBook = { book ->
                        when (book) {
                            is SearchUiState.BookUiModel.SearchedBookUiModel -> {
                                navigateToSearchedDetail(book.searchedBook)
                            }

                            is SearchUiState.BookUiModel.CollectedBookUiModel -> {
                                navigateToCollectedDetail(book.collectedBook.book.id)
                            }
                        }
                    },
                    windowInsets = bottomWindowInsets,
                )
            }
        }

        val snackbarHostState = remember { SnackbarHostState() }
        val tooManyRequestErrorText = stringResource(Res.string.search_book_error_google_too_many_requests)
        LaunchedEffect(Unit) {
            viewModel.uiEvent.collectLatest { event ->
                when (event) {
                    SearchUiEvent.TooManyRequestsError -> {
                        snackbarHostState.showSnackbar(
                            message = tooManyRequestErrorText,
                            duration = SnackbarDuration.Long
                        )
                    }
                }
            }
        }
        SnackbarHost(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(bottomWindowInsets)
                .align(Alignment.BottomCenter),
            hostState = snackbarHostState
        ) {
            Snackbar(it)
        }
    }
}

@Composable
private fun SearchTopAppBar(
    isSearching: Boolean,
    initialQuery: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        if (initialQuery.isEmpty()) focusRequester.requestFocus()
    }

    Column(modifier = modifier) {
        SearchField(
            modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
            initialValue = initialQuery,
            onValueChange = onQueryChange,
            placeholder = stringResource(Res.string.search_book_placeholder),
            isSearching = isSearching,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            modifier = Modifier.offset(x = (-16).dp).align(Alignment.End),
            text = stringResource(Res.string.powered_by_google),
            color = System.colors.onSurfaceVariant,
            fontSize = 12.sp,
        )
    }
}

@Composable
private fun Dashboard(
    modifier: Modifier = Modifier,
    navigateToCollectedBookDetail: (BookId) -> Unit,
) {
    Column(modifier = modifier.padding(top = 64.dp)) {
        AddByLinkCard(navigateToCollectedBookDetail = navigateToCollectedBookDetail)
    }
}

@Composable
private fun SearchedBooksList(
    books: List<SearchUiState.BookUiModel>,
    onClickBook: (SearchUiState.BookUiModel) -> Unit,
    windowInsets: WindowInsets,
    modifier: Modifier = Modifier,
) {
    val state = rememberLazyListState()
    val focusManager = LocalFocusManager.current
    LaunchedEffect(focusManager) {
        snapshotFlow { state.isScrollInProgress }
            .distinctUntilChanged()
            .collectLatest { if (it) focusManager.clearFocus() }
    }

    LazyColumn(
        modifier = modifier,
        state = state,
        verticalArrangement = Arrangement.spacedBy(32.dp),
        contentPadding = windowInsets.asPaddingValues() + PaddingValues(top = 64.dp),
    ) {
        items(books, key = { it.book.id }) { book ->
            Book(
                book = book,
                onClick = { onClickBook(book) },
            )
        }
    }
}

@Composable
private fun Book(
    book: SearchUiState.BookUiModel,
    onClick: () -> Unit,
) {
    BookDetail(
        cover = {
            BookCover(
                book = book.book,
                layout = BookCoverDefaults.layout(BookCoverLayoutStyle.FillBy.Height.Medium),
            )
        },
        title = {
            BookTitle(
                book = book.book,
                additional = {
                    if (book is SearchUiState.BookUiModel.CollectedBookUiModel) {
                        CollectedText()
                    }
                }
            )
        },
        onClick = onClick,
    )
}

@Composable
private fun CollectedText(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.CheckSmall,
            contentDescription = null,
            tint = System.colors.collected,
        )
        Text(
            text = stringResource(Res.string.book_collected),
            fontSize = 14.sp,
            color = System.colors.collected,
        )
    }
}

@Composable
private fun AddByLinkCard(modifier: Modifier = Modifier, navigateToCollectedBookDetail: (BookId) -> Unit) {
    var sheetVisible by remember { mutableStateOf(false) }
    if (sheetVisible) {
        AddByLinkSheet(
            onDismissRequest = { sheetVisible = false },
            navigateToCollectedBookDetail = navigateToCollectedBookDetail,
        )
    }
    InteractionBox(
        modifier = modifier.fillMaxWidth().clickable { sheetVisible = true },
        colors = InteractionBoxDefaults.colorsOfSecondary()
    ) {
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Language, contentDescription = null)
                Text(
                    stringResource(Res.string.search_book_add_by_link_books_tw_title),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                )
            }
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(System.colors.primary)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            ) {
                Text(
                    text = stringResource(Res.string.new_tag),
                    color = System.colors.onPrimary,
                    fontSize = 12.sp,
                    lineHeight = 12.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}