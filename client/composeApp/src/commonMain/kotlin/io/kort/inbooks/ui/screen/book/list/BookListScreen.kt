package io.kort.inbooks.ui.screen.book.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.kort.inbooks.ui.resource.Res
import io.kort.inbooks.ui.resource.book_list_empty_description
import io.kort.inbooks.ui.resource.book_list_empty_title
import io.kort.inbooks.ui.resource.book_list_title
import io.kort.inbooks.ui.resource.illustration_empty_book_list
import io.kort.inbooks.app.di.getViewModel
import io.kort.inbooks.domain.model.book.CollectedBook
import io.kort.inbooks.domain.model.book.LocalYearAndMonth
import io.kort.inbooks.domain.model.settings.BookListDisplayStyle
import io.kort.inbooks.ui.component.Button
import io.kort.inbooks.ui.component.ButtonDefaults
import io.kort.inbooks.ui.component.PageScope
import io.kort.inbooks.ui.component.TopAppBar
import io.kort.inbooks.ui.foundation.DateTimeFormatter
import io.kort.inbooks.ui.foundation.SharedScene
import io.kort.inbooks.ui.foundation.plus
import io.kort.inbooks.ui.pattern.Empty
import io.kort.inbooks.ui.pattern.book.BookCover
import io.kort.inbooks.ui.pattern.book.BookCoverDefaults
import io.kort.inbooks.ui.pattern.book.BookCoverLayoutStyle
import io.kort.inbooks.ui.pattern.book.BookDetail
import io.kort.inbooks.ui.pattern.book.BookDetailDefaults
import io.kort.inbooks.ui.pattern.book.BookTitle
import io.kort.inbooks.ui.resource.Icons
import io.kort.inbooks.ui.resource.ViewGrid
import io.kort.inbooks.ui.resource.ViewRow2
import io.kort.inbooks.ui.token.system.System
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun PageScope.BookListScreen(
    navigateToCollectedBookDetail: (CollectedBook) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = getViewModel<BookListViewModel>()
    val bottomWindowInsets = windowInsets.only(WindowInsetsSides.Bottom)
    val excludeBottomWindowInsets = windowInsets.exclude(bottomWindowInsets)
    val uiState by viewModel.uiState.collectAsState()
    Column(modifier.windowInsetsPadding(excludeBottomWindowInsets)) {
        val initializedUiStateOrNull = remember(uiState) { uiState as? BookListUiState.Initialized }
        TopAppBar(
            showChangeDisplayStyleButton = initializedUiStateOrNull?.books.isNullOrEmpty().not(),
            currentDisplayStyle = initializedUiStateOrNull?.displayStyle ?: BookListDisplayStyle.Grid,
            onDisplayStyleChange = { uiState.intentTo(BookListUiIntent.UpdateDisplayStyle(it)) },
        )
        initializedUiStateOrNull?.let { initializedUiState ->
            if (initializedUiState.books.isEmpty()) {
                Empty(
                    modifier = Modifier.weight(1f).windowInsetsPadding(bottomWindowInsets),
                    illustration = painterResource(Res.drawable.illustration_empty_book_list),
                    title = stringResource(Res.string.book_list_empty_title),
                    description = stringResource(Res.string.book_list_empty_description)
                )
            } else {
                Spacer(Modifier.height(32.dp))
                LazyColumn(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentPadding = bottomWindowInsets.asPaddingValues() + PaddingValues(top = 32.dp),
                ) {
                    initializedUiState.sortedDate.forEachIndexed { index, date ->
                        item(key = "${date.year}/${date.month}") {
                            val isLast = index == initializedUiState.sortedDate.lastIndex
                            Column(
                                modifier = Modifier
                                    .animateItem(placementSpec = null)
                                    .padding(bottom = if (isLast) 0.dp else 64.dp),
                                verticalArrangement = Arrangement.spacedBy(24.dp),
                            ) {
                                Date(date)
                                Books(
                                    displayStyle = initializedUiState.displayStyle,
                                    books = initializedUiState.books[date]!!,
                                    onBookClick = { navigateToCollectedBookDetail(it) },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TopAppBar(
    showChangeDisplayStyleButton: Boolean,
    currentDisplayStyle: BookListDisplayStyle?,
    onDisplayStyleChange: (BookListDisplayStyle) -> Unit,
) {
    TopAppBar(
        start = {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(Res.string.book_list_title),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = System.colors.onSurface,
            )
        },
        end = {
            if (showChangeDisplayStyleButton && currentDisplayStyle != null) {
                ChangeViewButton(
                    currentDisplayStyle = currentDisplayStyle,
                    onDisplayStyleChange = onDisplayStyleChange,
                )
            }
        },
    )
}

@Composable
private fun ChangeViewButton(
    currentDisplayStyle: BookListDisplayStyle,
    onDisplayStyleChange: (BookListDisplayStyle) -> Unit,
) {
    Button(
        onClick = {
            onDisplayStyleChange(
                if (currentDisplayStyle == BookListDisplayStyle.List) BookListDisplayStyle.Grid
                else BookListDisplayStyle.List
            )
        },
        colors = ButtonDefaults.secondaryButtonColors(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BookListDisplayStyle.entries.forEachIndexed { index, view ->
                Icon(
                    imageVector = when (view) {
                        BookListDisplayStyle.List -> Icons.ViewRow2
                        BookListDisplayStyle.Grid -> Icons.ViewGrid
                    },
                    contentDescription = null,
                    tint = if (currentDisplayStyle == view) System.colors.onSecondary else System.colors.onSecondaryVariant,
                )
                if (index != BookListDisplayStyle.entries.lastIndex) {
                    Spacer(
                        Modifier
                            .width(1.5.dp)
                            .height(10.dp)
                            .clip(CircleShape)
                            .background(System.colors.onSecondaryVariant)
                    )
                }
            }
        }
    }
}

@Composable
private fun Date(date: LocalYearAndMonth) {
    Column {
        val currentYear =
            remember {
                Clock.System
                    .now()
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                    .year
            }
        if (date.year != currentYear) {
            Text(
                text = date.year.toString(),
                color = System.colors.onSurfaceVariant,
                fontSize = 20.sp,
            )
        }
        Spacer(Modifier.height(16.dp))
        val monthText =
            remember(date.month) {
                val timeZone = TimeZone.currentSystemDefault()
                DateTimeFormatter.formatByPattern(
                    instant = LocalDate(2024, date.month, 1).atStartOfDayIn(timeZone),
                    pattern = "MMMM",
                )
            }
        Text(
            text = monthText,
            color = System.colors.onSurface,
            fontSize = 16.sp,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Books(
    displayStyle: BookListDisplayStyle,
    books: List<CollectedBook>,
    onBookClick: (CollectedBook) -> Unit,
) {
    val verticalSpace = remember(displayStyle) {
        when (displayStyle) {
            BookListDisplayStyle.Grid -> 24.dp
            BookListDisplayStyle.List -> 32.dp
        }
    }

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalArrangement = Arrangement.spacedBy(verticalSpace),
    ) {
        books.forEach { book ->
            key(book.book.id) {
                BookDetail(
                    cover = {
                        BookCover(
                            book = book.book,
                            onClick = { onBookClick(book) },
                            layout = BookCoverDefaults.layout(fillBy = BookCoverLayoutStyle.FillBy.Height.Medium),
                            share = BookCoverDefaults.share(listOf(SharedScene.BookListAndBookDetail)),
                        )
                    },
                    title = {
                        if (displayStyle == BookListDisplayStyle.List) {
                            BookTitle(book = book.book)
                        }
                    },
                    additional = {
                        if (displayStyle == BookListDisplayStyle.List) {
                            BookDetailDefaults.Reason(book.readingReason)
                        }
                    },
                    onClick = { onBookClick(book) },
                )
            }
        }
    }
}
