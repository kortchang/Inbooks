package io.kort.inbooks.ui.screen.topic.edit.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.kort.inbooks.ui.resource.Res
import io.kort.inbooks.ui.resource.add
import io.kort.inbooks.ui.resource.add_or_edit_topic_add_book_button_text
import io.kort.inbooks.ui.resource.add_or_edit_topic_description
import io.kort.inbooks.ui.resource.add_or_edit_topic_description_placeholder
import io.kort.inbooks.ui.resource.add_or_edit_topic_edit_books_button_text
import io.kort.inbooks.ui.resource.add_or_edit_topic_title
import io.kort.inbooks.ui.resource.add_or_edit_topic_title_placeholder
import io.kort.inbooks.ui.resource.add_or_edit_topic_unnamed_title
import io.kort.inbooks.ui.resource.save
import io.kort.inbooks.domain.model.book.CollectedBook
import io.kort.inbooks.domain.model.topic.TopicBook
import io.kort.inbooks.ui.component.Button
import io.kort.inbooks.ui.component.ButtonDefaults
import io.kort.inbooks.ui.component.PageScope
import io.kort.inbooks.ui.component.TextField
import io.kort.inbooks.ui.component.TopAppBar
import io.kort.inbooks.ui.pattern.book.BookCover
import io.kort.inbooks.ui.pattern.book.BookDetail
import io.kort.inbooks.ui.pattern.book.BookTitle
import io.kort.inbooks.ui.resource.Check
import io.kort.inbooks.ui.resource.EditPencil
import io.kort.inbooks.ui.resource.Icons
import io.kort.inbooks.ui.resource.Plus
import io.kort.inbooks.ui.resource.Xmark
import org.jetbrains.compose.resources.stringResource

@Composable
fun PageScope.AddOrEditTopicScreen(
    viewModel: AddOrEditTopicViewModel,
    back: () -> Boolean,
    navigateToSelectBook: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(viewModel.uiEvent, back) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                AddOrEditTopicUiEvent.Confirm, AddOrEditTopicUiEvent.TopicNotFound -> back()
            }
        }
    }

    val bottomWindowInsets = windowInsets.only(WindowInsetsSides.Bottom)
    val excludeBottomWindowInsets = windowInsets.exclude(bottomWindowInsets)
    Column(modifier.windowInsetsPadding(excludeBottomWindowInsets)) {
        val unnamedTitle = stringResource(Res.string.add_or_edit_topic_unnamed_title)
        AddOrEditTopicTopAppBar(
            uiState = uiState,
            back = { back() },
            confirmEnable = uiState.confirmEnable,
            confirm = { uiState.intentTo(AddOrEditTopicUiIntent.Confirm(unnamedTitle)) }
        )
        Spacer(Modifier.height(24.dp))
        (uiState as? AddOrEditTopicUiState.InitializedUiState)?.let { initializedUiState ->
            LazyColumn(modifier = Modifier.weight(1f)) {
                item {
                    TextContent(
                        initialTitle = initializedUiState.topic.title,
                        initialDescription = initializedUiState.topic.description,
                        onTitleChange = { initializedUiState.intentTo(AddOrEditTopicUiIntent.UpdateTitle(it)) },
                        onDescriptionChange = {
                            initializedUiState.intentTo(AddOrEditTopicUiIntent.UpdateDescription(it))
                        }
                    )
                }

                item {
                    Spacer(
                        Modifier
                            .padding(vertical = 32.dp)
                            .height(1.dp).fillMaxWidth()
                            .background(io.kort.inbooks.ui.token.system.System.colors.outline)
                    )
                }

                books(
                    books = initializedUiState.topic.books,
                    bookIdToCollectedBooks = initializedUiState.bookIdToCollectedBooks,
                    onClickAddOrEdit = { navigateToSelectBook() }
                )
            }
        }
    }
}

@Composable
private fun AddOrEditTopicTopAppBar(
    uiState: AddOrEditTopicUiState,
    back: () -> Unit,
    confirmEnable: Boolean,
    confirm: () -> Unit,
) {
    val isEdit = uiState is AddOrEditTopicUiState.Edit

    CompositionLocalProvider(LocalContentColor provides io.kort.inbooks.ui.token.system.System.colors.onSecondary) {
        TopAppBar(
            start = {
                Button(
                    start = {
                        Icon(
                            imageVector = Icons.Xmark,
                            contentDescription = null
                        )
                    },
                    onClick = back
                )
            },
            center = {
                Spacer(Modifier.weight(1f))
            },
            end = {
                Button(
                    start = {
                        Icon(
                            imageVector = if (isEdit) Icons.Check else Icons.Plus,
                            contentDescription = null
                        )
                    },
                    text = {
                        Text(
                            text = stringResource(if (isEdit) Res.string.save else Res.string.add)
                        )
                    },
                    onClick = confirm,
                    colors = ButtonDefaults.colorsOfSecondary(),
                    enabled = confirmEnable,
                )
            }
        )
    }
}

@Composable
private fun TextContent(
    initialTitle: String,
    initialDescription: String,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        TextField(
            title = stringResource(Res.string.add_or_edit_topic_title),
            initialValue = initialTitle,
            onValueChange = onTitleChange,
            placeholder = stringResource(Res.string.add_or_edit_topic_title_placeholder),
        )
        TextField(
            title = stringResource(Res.string.add_or_edit_topic_description),
            initialValue = initialDescription,
            onValueChange = onDescriptionChange,
            placeholder = stringResource(Res.string.add_or_edit_topic_description_placeholder),
        )
    }
}

private fun LazyListScope.books(
    books: List<TopicBook>,
    bookIdToCollectedBooks: Map<String, CollectedBook>,
    onClickAddOrEdit: () -> Unit,
) {
    item {
        val isAdd = books.isNotEmpty()
        Button(
            modifier = Modifier.fillMaxWidth(),
            start = {
                Icon(
                    imageVector = if (isAdd) Icons.EditPencil else Icons.Plus,
                    contentDescription = null
                )
            },
            text = {
                Text(
                    text = stringResource(
                        if (isAdd) Res.string.add_or_edit_topic_edit_books_button_text
                        else Res.string.add_or_edit_topic_add_book_button_text
                    )
                )
            },
            onClick = onClickAddOrEdit,
            colors = ButtonDefaults.colorsOfSecondary(),
        )
    }

    item {
        Spacer(Modifier.height(32.dp))
    }

    items(
        items = books.reversed(),
        key = { it.book.id },
    ) { topicBook ->
        BookDetail(
            modifier = Modifier.fillMaxWidth(),
            cover = { BookCover(book = topicBook.book) },
            title = { BookTitle(book = topicBook.book) },
            additional = {
                val reasonOrNull = bookIdToCollectedBooks[topicBook.book.id]?.readingReason
                if (reasonOrNull != null) {
                    Text(
                        text = reasonOrNull,
                        fontSize = 14.sp,
                        color = io.kort.inbooks.ui.token.system.System.colors.onSurface,
                    )
                }
            }
        )
    }
}