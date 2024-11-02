package io.kort.inbooks.ui.screen.topic.detail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberBasicTooltipState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.touchlab.kermit.Logger
import io.kort.inbooks.ui.resource.Res
import io.kort.inbooks.ui.resource.add_or_edit_topic_confirm_delete_title
import io.kort.inbooks.ui.resource.cancel
import io.kort.inbooks.ui.resource.delete
import io.kort.inbooks.ui.resource.edit
import io.kort.inbooks.domain.model.book.Book
import io.kort.inbooks.domain.model.book.BookId
import io.kort.inbooks.domain.model.book.CollectedBook
import io.kort.inbooks.domain.model.topic.Topic
import io.kort.inbooks.domain.model.topic.TopicBook
import io.kort.inbooks.ui.component.Button
import io.kort.inbooks.ui.component.ButtonDefaults
import io.kort.inbooks.ui.component.IconButton
import io.kort.inbooks.ui.component.PageScope
import io.kort.inbooks.ui.foundation.SharedScene
import io.kort.inbooks.ui.foundation.plus
import io.kort.inbooks.ui.pattern.MoreMenuBox
import io.kort.inbooks.ui.pattern.MoreMenuItem
import io.kort.inbooks.ui.pattern.book.BookCover
import io.kort.inbooks.ui.pattern.book.BookCoverDefaults
import io.kort.inbooks.ui.pattern.book.BookDetail
import io.kort.inbooks.ui.pattern.book.BookDetailDefaults
import io.kort.inbooks.ui.pattern.book.BookTitle
import io.kort.inbooks.ui.resource.EditPencil
import io.kort.inbooks.ui.resource.Icons
import io.kort.inbooks.ui.resource.MoreVert
import io.kort.inbooks.ui.resource.NavArrowLeft
import io.kort.inbooks.ui.resource.Trash
import io.kort.inbooks.ui.token.system.System
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PageScope.TopicDetailScreen(
    viewModel: TopicDetailViewModel,
    back: () -> Unit,
    navigateToEditTopic: () -> Unit,
    navigateToCollectedBookDetail: (book: CollectedBook) -> Unit,
    navigateToSearchedBookDetail: (book: Book) -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedUiEventHandler(
        uiEvent = viewModel.uiEvent,
        back = back,
    )
    val bottomWindowInsets = windowInsets.only(WindowInsetsSides.Bottom)
    val excludeBottomWindowInsets = windowInsets.exclude(bottomWindowInsets)

    (uiState as? TopicDetailUiState.Initialized)?.let { initializedUiState ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(32.dp),
            contentPadding = windowInsets.exclude(windowInsets.only(WindowInsetsSides.Top)).asPaddingValues(),
        ) {
            stickyHeader {
                TopAppBar(
                    modifier = Modifier.windowInsetsPadding(windowInsets.only(WindowInsetsSides.Top)),
                    onBack = back,
                    onShare = { },
                    onEdit = navigateToEditTopic,
                    onDelete = { initializedUiState.intentTo(TopicDetailUiIntent.Delete) },
                )
            }

            Content(
                topic = initializedUiState.topic,
                bookIdToCollectedBook = initializedUiState.bookIdToCollectedBook,
                onCollectedBookClick = navigateToCollectedBookDetail,
                onSearchedBookClick = navigateToSearchedBookDetail,
            )
        }
    }
}

@Composable
private fun LaunchedUiEventHandler(
    uiEvent: SharedFlow<TopicDetailUiEvent>,
    back: () -> Unit
) {
    val updatedBack by rememberUpdatedState(back)
    LaunchedEffect(Unit) {
        uiEvent.collect { event ->
            when (event) {
                TopicDetailUiEvent.TopicNotExist, TopicDetailUiEvent.TopicDeleted -> updatedBack()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TopAppBar(
    onBack: () -> Unit,
    onShare: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    io.kort.inbooks.ui.component.TopAppBar(
        modifier = modifier,
        start = {
            Button(
                start = {
                    Icon(
                        imageVector = Icons.NavArrowLeft,
                        contentDescription = null,
                    )
                },
                onClick = onBack,
            )
        },
        center = { Spacer(Modifier.weight(1f)) },
        end = {
            var deleteDialogVisible by remember { mutableStateOf(false) }
            // TODO("等到有 server 再來做分享")
//            IconButton(
//                icon = Icons.Share,
//                onClick = onShare,
//                colors = ButtonDefaults.colorsOfSecondary()
//            )
            val tooltipState = rememberBasicTooltipState(false)
            MoreMenuBox(
                state = tooltipState,
                menuContent = {
                    MoreMenuItem(
                        icon = Icons.EditPencil,
                        text = stringResource(Res.string.edit),
                        onClick = {
                            onEdit()
                            tooltipState.dismiss()
                        },
                    )
                    MoreMenuItem(
                        icon = Icons.Trash,
                        text = stringResource(Res.string.delete),
                        onClick = {
                            deleteDialogVisible = true
                            tooltipState.dismiss()
                        },
                        color = System.colors.warning,
                    )
                },
            ) {
                val coroutineScope = rememberCoroutineScope()
                IconButton(
                    icon = Icons.MoreVert,
                    onClick = { coroutineScope.launch { tooltipState.show() } },
                    colors = ButtonDefaults.secondaryButtonColors(),
                )

                if (deleteDialogVisible) {
                    AlertDialog(
                        containerColor = System.colors.background,
                        titleContentColor = System.colors.onBackground,
                        onDismissRequest = { deleteDialogVisible = false },
                        title = {
                            Text(
                                fontSize = 16.sp,
                                text = stringResource(Res.string.add_or_edit_topic_confirm_delete_title)
                            )
                        },
                        confirmButton = {
                            Button(
                                start = {
                                    Text(
                                        text = stringResource(Res.string.delete),
                                        fontWeight = FontWeight.Medium
                                    )
                                },
                                onClick = {
                                    onDelete()
                                },
                                colors = ButtonDefaults.warningTextButtonColors(),
                            )
                        },
                        dismissButton = {
                            Button(
                                start = {
                                    Text(
                                        text = stringResource(Res.string.cancel),
                                        fontWeight = FontWeight.Medium,
                                        color = System.colors.onBackgroundVariant
                                    )
                                },
                                onClick = { deleteDialogVisible = false },
                            )
                        },
                    )
                }
            }
        },
    )
}

private fun LazyListScope.Content(
    topic: Topic,
    bookIdToCollectedBook: Map<BookId, CollectedBook>,
    onCollectedBookClick: (book: CollectedBook) -> Unit,
    onSearchedBookClick: (book: Book) -> Unit,
) {
    item {
        Column {
            Spacer(Modifier.height(32.dp))
            TopicInformation(topic = topic)
        }
    }

    books(
        books = topic.books,
        bookIdToCollectedBook = bookIdToCollectedBook,
        onCollectedBookClick = onCollectedBookClick,
        onSearchedBookClick = onSearchedBookClick,
    )
}

@Composable
private fun TopicInformation(
    topic: Topic,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = topic.title,
            color = System.colors.onSurface,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
        )
        if (topic.description.isNotBlank()) {
            Text(
                text = topic.description,
                color = System.colors.onSurfaceVariant,
                fontSize = 16.sp,
            )
        }
    }
}

private fun LazyListScope.books(
    books: List<TopicBook>,
    bookIdToCollectedBook: Map<BookId, CollectedBook>,
    onCollectedBookClick: (book: CollectedBook) -> Unit,
    onSearchedBookClick: (book: Book) -> Unit,
) {
    items(
        items = books.reversed(),
        key = { topicBook -> topicBook.book.id },
    ) { topicBook ->
        val collectedBookOrNull = remember(topicBook, bookIdToCollectedBook) {
            bookIdToCollectedBook[topicBook.book.id]
        }
        BookDetail(
            modifier = Modifier.animateItem(),
            cover = {
                BookCover(
                    book = topicBook.book,
                    share = BookCoverDefaults.share(SharedScenes),
                )
            },
            title = { BookTitle(book = topicBook.book) },
            additional = {
                BookDetailDefaults.Reason(collectedBookOrNull?.readingReason)
            },
            onClick = {
                if (collectedBookOrNull != null) {
                    onCollectedBookClick(collectedBookOrNull)
                } else {
                    onSearchedBookClick(topicBook.book)
                }
            },
        )
    }
}

private val SharedScenes = listOf(
    SharedScene.TopicDetailAndBookDetail
)