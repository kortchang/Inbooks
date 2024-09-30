package io.kort.inbooks.ui.screen.book.detail.screen.collected

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberBasicTooltipState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.kort.inbooks.ui.resource.Res
import io.kort.inbooks.ui.resource.collected_book_book_information_title
import io.kort.inbooks.ui.resource.collected_book_reading_reason_placeholder
import io.kort.inbooks.ui.resource.collected_book_reading_reason_title
import io.kort.inbooks.ui.resource.collected_book_uncollect
import io.kort.inbooks.app.di.getViewModel
import io.kort.inbooks.domain.model.book.Book
import io.kort.inbooks.domain.model.book.CollectedBook
import io.kort.inbooks.ui.component.Button
import io.kort.inbooks.ui.component.ButtonDefaults
import io.kort.inbooks.ui.component.InteractionBox
import io.kort.inbooks.ui.component.PageScope
import io.kort.inbooks.ui.component.TextField
import io.kort.inbooks.ui.component.TopAppBar
import io.kort.inbooks.ui.screen.book.detail.pattern.BookInformation
import io.kort.inbooks.ui.foundation.SharedScene
import io.kort.inbooks.ui.pattern.MoreMenuBox
import io.kort.inbooks.ui.pattern.MoreMenuItem
import io.kort.inbooks.ui.pattern.book.BookCover
import io.kort.inbooks.ui.pattern.book.BookCoverDefaults
import io.kort.inbooks.ui.pattern.book.BookCoverLayoutStyle
import io.kort.inbooks.ui.pattern.book.BookDetail
import io.kort.inbooks.ui.pattern.book.BookTitle
import io.kort.inbooks.ui.resource.Icons
import io.kort.inbooks.ui.resource.Minus
import io.kort.inbooks.ui.resource.MinusCircle
import io.kort.inbooks.ui.resource.MoreVert
import io.kort.inbooks.ui.resource.NavArrowDown
import io.kort.inbooks.ui.resource.NavArrowLeft
import io.kort.inbooks.ui.screen.book.detail.pattern.BookInformationDefaults
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.core.parameter.parametersOf

@Composable
fun PageScope.CollectedBookDetailScreen(
    bookId: String,
    back: () -> Unit,
    popAndNavigateToSearchedBookDetail: (book: Book) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = getViewModel<CollectedBookDetailViewModel> { parametersOf(bookId) }
    val uiState by viewModel.uiState.collectAsState()
    val bookOrNull = uiState.book
    bookOrNull?.let { book ->
        LaunchedHandleUiEventEffect(
            uiEventFlow = viewModel.uiEvent,
            navigateToSearchedBookDetail = { popAndNavigateToSearchedBookDetail(book.book) }
        )

        val bottomWindowInsets = windowInsets.only(WindowInsetsSides.Bottom)
        val excludeBottomWindowInsets = windowInsets.exclude(bottomWindowInsets)
        Column(modifier.fillMaxSize().windowInsetsPadding(excludeBottomWindowInsets)) {
            CollectedBookDetailTopAppBar(
                back = back,
                uncollect = {
                    uiState.intentTo(CollectedBookDetailUiIntent.UnCollect)
                }
            )
            Content(
                modifier = Modifier.fillMaxHeight(),
                book = book,
                onReadingReasonChange = { reason ->
                    uiState.intentTo(CollectedBookDetailUiIntent.UpdateReadingReason(reason))
                },
                windowInsets = bottomWindowInsets,
            )
        }
    }
}

@Composable
private fun LaunchedHandleUiEventEffect(
    uiEventFlow: Flow<CollectedBookDetailUiEvent>,
    navigateToSearchedBookDetail: () -> Unit,
) {
    LaunchedEffect(uiEventFlow) {
        uiEventFlow.collect { event ->
            when (event) {
                CollectedBookDetailUiEvent.UnCollected -> navigateToSearchedBookDetail()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CollectedBookDetailTopAppBar(
    back: () -> Unit,
    uncollect: () -> Unit,
) {
    TopAppBar(
        start = {
            Button(
                onClick = back,
                start = { Icon(Icons.NavArrowLeft, contentDescription = null) }
            )
        },
        center = { Spacer(Modifier.weight(1f)) },
        end = {
            val moreMenuState = rememberBasicTooltipState()
            MoreMenuBox(
                state = moreMenuState,
                menuContent = {
                    MoreMenuItem(
                        icon = Icons.MinusCircle,
                        text = stringResource(Res.string.collected_book_uncollect),
                        onClick = {
                            uncollect()
                            moreMenuState.dismiss()
                        },
                        color = io.kort.inbooks.ui.token.system.System.colors.warning,
                    )
                }
            ) {
                val coroutineScope = rememberCoroutineScope()
                Button(
                    onClick = { coroutineScope.launch { moreMenuState.show() } },
                    start = { Icon(Icons.MoreVert, contentDescription = null) },
                    colors = ButtonDefaults.colorsOfSecondary(),
                )
            }
        }
    )
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    book: CollectedBook,
    onReadingReasonChange: (String) -> Unit,
    windowInsets: WindowInsets,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .windowInsetsPadding(windowInsets),
    ) {
        Spacer(Modifier.height(48.dp))
        Book(book.book)
        Spacer(Modifier.height(48.dp))
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            TextField(
                title = stringResource(Res.string.collected_book_reading_reason_title),
                initialValue = book.readingReason,
                onValueChange = onReadingReasonChange,
                placeholder = stringResource(Res.string.collected_book_reading_reason_placeholder),
            )

            val interaction = remember { MutableInteractionSource() }
            var expand by remember { mutableStateOf(false) }
            InteractionBox(
                modifier = Modifier
                    .indication(interaction, LocalIndication.current),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(
                        modifier = Modifier
                            .clickable(interactionSource = interaction, indication = null) { expand = !expand }
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = stringResource(Res.string.collected_book_book_information_title),
                            color = io.kort.inbooks.ui.token.system.System.colors.onBackground,
                            fontSize = 16.sp,
                        )
                        Icon(
                            imageVector = if (expand) Icons.Minus else Icons.NavArrowDown,
                            contentDescription = null,
                            tint = io.kort.inbooks.ui.token.system.System.colors.onBackgroundVariant,
                        )
                    }
                    if (expand) {
                        BookInformation(book.book, colors = BookInformationDefaults.colorsOnBackground())
                    }
                }
            }
        }
    }
}

@Composable
private fun Book(book: Book) {
    BookDetail(
        cover = {
            BookCover(
                book = book,
                layout = BookCoverDefaults.layout(fillBy = BookCoverLayoutStyle.FillBy.Width.Large),
                share = BookCoverDefaults.share(SharedScene.ForBookDetail),
            )
        },
        title = {
            BookTitle(
                book = book,
            )
        }
    )
}