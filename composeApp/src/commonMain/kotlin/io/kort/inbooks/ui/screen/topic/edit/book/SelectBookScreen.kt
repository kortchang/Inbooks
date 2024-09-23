package io.kort.inbooks.ui.screen.topic.edit.book

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import io.kort.inbooks.ui.resource.Res
import io.kort.inbooks.ui.resource.confirm
import coil3.compose.AsyncImage
import io.kort.inbooks.domain.model.book.Book
import io.kort.inbooks.domain.model.book.BookId
import io.kort.inbooks.domain.model.book.CollectedBook
import io.kort.inbooks.domain.model.book.LocalYearAndMonth
import io.kort.inbooks.domain.model.topic.TopicBook
import io.kort.inbooks.ui.component.Button
import io.kort.inbooks.ui.component.ButtonDefaults
import io.kort.inbooks.ui.component.PageScope
import io.kort.inbooks.ui.component.SelectBox
import io.kort.inbooks.ui.component.TopAppBar
import io.kort.inbooks.ui.foundation.DateTimeFormatter
import io.kort.inbooks.ui.foundation.plus
import io.kort.inbooks.ui.pattern.book.BookCover
import io.kort.inbooks.ui.pattern.book.BookDetail
import io.kort.inbooks.ui.pattern.book.BookTitle
import io.kort.inbooks.ui.resource.Check
import io.kort.inbooks.ui.resource.Icons
import io.kort.inbooks.ui.resource.Xmark
import io.kort.inbooks.ui.token.reference.Reference
import io.kort.inbooks.ui.token.system.System
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource

@Composable
fun PageScope.SelectBookScreen(
    viewModel: SelectBookViewModel,
    back: () -> Unit,
    confirm: (books: List<TopicBook>) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val bottomWindowInsets = windowInsets.only(WindowInsetsSides.Bottom)
    val excludeBottomWindowInsets = windowInsets.exclude(bottomWindowInsets)
    Column(Modifier.windowInsetsPadding(excludeBottomWindowInsets)) {
        (uiState as? SelectBookUiState.Initialized)?.let { initializedUiState ->
            TopAppBar(
                selectBooks = initializedUiState.selectedBooks,
                back = back,
                confirm = { confirm(initializedUiState.selectedBooks) }
            )
            Books(
                modifier = Modifier.weight(1f),
                windowInsets = bottomWindowInsets,
                selectedBooks = initializedUiState.selectedBooks,
                books = initializedUiState.books,
                bookIdToCollectedBooks = initializedUiState.bookIdToCollectedBooks,
                onSelectedChange = { book, selected ->
                    initializedUiState.intentTo(SelectBookUiIntent.UpdateBookSelected(book, selected))
                }
            )
        }
    }
}

@Composable
private fun TopAppBar(
    selectBooks: List<TopicBook>,
    back: () -> Unit,
    confirm: () -> Unit,
) {
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
            ConfirmButton(
                selectedBooks = selectBooks,
                onClick = confirm
            )
        }
    )
}

@Composable
private fun Book(
    modifier: Modifier = Modifier,
    selected: Boolean,
    book: Book,
    reason: String?,
    onSelectedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        SelectBox(
            selected = selected,
            onSelectedChange = onSelectedChange
        )
        BookDetail(
            modifier = Modifier.weight(1f),
            cover = { BookCover(book = book) },
            title = { BookTitle(book = book) },
            additional = {
                if (reason != null) {
                    Text(
                        text = reason,
                        fontSize = 14.sp,
                        color = System.colors.onSurface,
                    )
                }
            },
            onClick = {
                onSelectedChange(!selected)
            }
        )
    }
}

@Composable
private fun Books(
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets,
    books: Map<LocalYearAndMonth, List<Book>>,
    bookIdToCollectedBooks: Map<BookId, CollectedBook>,
    selectedBooks: List<TopicBook>,
    onSelectedChange: (Book, Boolean) -> Unit
) {
    val selectedBooksSet = remember(selectedBooks) { selectedBooks.map { it.book }.toSet() }
    val lazyListState = rememberLazyListState()
    LazyColumn(
        modifier = modifier,
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(32.dp),
        contentPadding = windowInsets.asPaddingValues() + PaddingValues(top = 54.dp),
    ) {
        books.forEach { (yearAndMonth, books) ->
            item(key = yearAndMonth.toString(), contentType = "YearAndMonth") {
                YearAndMonth(
                    modifier = Modifier.fillMaxWidth().background(System.colors.surface),
                    date = yearAndMonth,
                    countOfSelected = books.count { selectedBooksSet.contains(it) }
                )
            }

            items(
                items = books,
                key = { it.id },
                contentType = { "Book" }
            ) { book ->
                val selected = remember(book, selectedBooksSet) { selectedBooksSet.contains(book) }
                Book(
                    modifier = Modifier.animateItem(),
                    selected = selected,
                    book = book,
                    reason = bookIdToCollectedBooks[book.id]?.readingReason,
                    onSelectedChange = { onSelectedChange(book, it) }
                )
            }
        }
    }
}

@Composable
private fun YearAndMonth(
    modifier: Modifier = Modifier,
    date: LocalYearAndMonth,
    countOfSelected: Int
) {
    Column(modifier = modifier) {
        val timeZone = remember { TimeZone.currentSystemDefault() }
        val currentYear = remember { Clock.System.now().toLocalDateTime(timeZone).year }
        if (date.year != currentYear) {
            val yearText = remember(date.year) {
                DateTimeFormatter.formatByPattern(
                    instant = LocalDate(date.year, date.month, 1).atStartOfDayIn(timeZone),
                    pattern = "YYYY"
                )
            }
            Text(
                text = yearText,
                color = System.colors.onSurfaceVariant,
                fontSize = 20.sp,
            )
        }
        Spacer(Modifier.height(16.dp))
        val monthText = remember(date.month) {
            DateTimeFormatter.formatByPattern(
                instant = LocalDate(date.year, date.month, 1).atStartOfDayIn(timeZone),
                pattern = "MMMM"
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = monthText,
                color = System.colors.onSurface,
                fontSize = 16.sp,
            )
            Box(
                Modifier
                    .background(System.colors.secondary, RoundedCornerShape(4.dp))
                    .padding(vertical = 4.dp, horizontal = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = countOfSelected.toString(),
                    color = System.colors.onSecondary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W600,
                )
            }
        }
    }
}

@Composable
private fun ConfirmButton(
    selectedBooks: List<TopicBook>,
    onClick: () -> Unit,
) {
    Button(
        start = {
            Icon(
                imageVector = Icons.Check,
                contentDescription = null
            )
        },
        text = {
            Text(
                text = stringResource(Res.string.confirm)
            )
        },
        end = {
            SaveButtonBookThumbnail(selectedBooks)
        },
        onClick = onClick,
        colors = ButtonDefaults.colorsOfSecondary(),
    )
}

@Composable
private fun SaveButtonBookThumbnail(
    selectedBooks: List<TopicBook>,
) {
    var displayBooks: List<Pair<TopicBook, Boolean>> by remember {
        mutableStateOf(selectedBooks.take(3).reversed().map { it to true })
    }

    LaunchedEffect(selectedBooks) {
        val removedBooks = displayBooks.map { it.first } - selectedBooks
        val addBooks = selectedBooks - displayBooks.map { it.first }

        var tempDisplayBooks = displayBooks
        removedBooks.forEach { book ->
            tempDisplayBooks = tempDisplayBooks.map { (b, visible) ->
                if (b == book) {
                    b to false
                } else {
                    b to visible
                }
            }
        }

        tempDisplayBooks = tempDisplayBooks + addBooks.map { it to true }
        displayBooks = tempDisplayBooks
    }

    if (displayBooks.isEmpty()) return
    AnimatedVisibility(displayBooks.isNotEmpty()) {
        Box(Modifier.padding(horizontal = 8.dp)) {
            displayBooks.forEachIndexed { index, (topicBook, targetVisible) ->
                key(topicBook) {
                    var visible by remember { mutableStateOf(false) }
                    var targetRotation by remember { mutableStateOf(0f) }
                    var targetOffset by remember { mutableStateOf(Offset.Zero) }
                    var dimAlpha by remember { mutableStateOf(0f) }
                    val density = LocalDensity.current
                    LaunchedEffect(displayBooks.lastIndex, index) {
                        val reverseIndex = displayBooks.lastIndex - index

                        if (reverseIndex <= 2) {
                            targetRotation = when (reverseIndex) {
                                0 -> 0f
                                1 -> 10f
                                2 -> -8f
                                else -> 0f
                            }

                            targetOffset = with(density) {
                                when (reverseIndex) {
                                    0 -> Offset.Zero
                                    1 -> Offset(2.dp.toPx(), -2.dp.toPx())
                                    2 -> Offset(-2.dp.toPx(), -3.dp.toPx())
                                    else -> Offset.Zero
                                }
                            }

                            dimAlpha = when (reverseIndex) {
                                0 -> 0f
                                1 -> 0.1f
                                2 -> 0.3f
                                else -> 0f
                            }
                        }
                    }
                    val animatedRotation by animateFloatAsState(targetRotation)
                    val animatedOffset by animateOffsetAsState(targetOffset)

                    LaunchedEffect(targetVisible) {
                        visible = targetVisible
                    }

                    AnimatedVisibility(
                        modifier = Modifier
                            .graphicsLayer {
                                translationX = animatedOffset.x
                                translationY = animatedOffset.y
                                rotationZ = animatedRotation
                                transformOrigin = TransformOrigin(0.5f, 1f)
                            }
                            .zIndex(index.toFloat())
                            .drawWithContent {
                                drawContent()
                                drawRect(
                                    color = Reference.Colors.Black.copy(alpha = dimAlpha),
                                    size = size,
                                    blendMode = BlendMode.Darken,
                                )
                            },
                        visible = visible,
                        enter = fadeIn() + slideInVertically { -it / 3 },
                        exit = fadeOut(),
                    ) {
                        DisposableEffect(topicBook, targetVisible) {
                            onDispose {
                                if (targetVisible.not()) {
                                    displayBooks = displayBooks.filterNot { it.first == topicBook }
                                }
                            }
                        }

                        AsyncImage(
                            modifier = Modifier
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(2.dp)),
                            model = topicBook.book.coverUrl,
                            contentDescription = null,
                            contentScale = ContentScale.FillHeight,
                        )
                    }
                }
            }
        }
    }
}