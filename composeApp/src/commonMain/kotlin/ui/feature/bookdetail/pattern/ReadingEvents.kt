package ui.feature.bookdetail.pattern

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Popup
import book.composeapp.generated.resources.Res
import book.composeapp.generated.resources.collected_book_reading_progress_placeholder
import book.composeapp.generated.resources.collected_book_reading_progress_title
import book.composeapp.generated.resources.collected_book_reading_reason_placeholder
import book.composeapp.generated.resources.icon_plus
import book.composeapp.generated.resources.icon_plus_small
import domain.book.CollectedBook
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.collectLatest
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.Padding
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ui.component.FloatButtonBox
import ui.feature.bookdetail.screen.collected.CollectedBookDetailContentScope
import ui.token.RubikFontFamily
import kotlin.math.absoluteValue

fun CollectedBookDetailContentScope.readingEvents(
    book: CollectedBook,
    onReadingEventAdd: (Int) -> Unit,
) {
    if (book.book.pageCount == null) return

    item {
        val events = remember(book.readingEvent) { book.readingEvent.toImmutableList() }
        DetailSection(
            title = {
                DetailSectionTitle(
                    icon = {
                        ReadingEventsIcon(events = events, pageCount = book.book.pageCount)
                    },
                    title = stringResource(Res.string.collected_book_reading_progress_title),
                    end = {
                        val latestPage by remember(book.readingEvent) {
                            derivedStateOf { book.readingEvent.maxOfOrNull { it.page } ?: 0 }
                        }
                        var showAddPopup by remember { mutableStateOf(false) }
                        if (latestPage < book.book.pageCount) {
                            Box(
                                Modifier.clickable { showAddPopup = true }) {
                                Image(
                                    modifier = Modifier.size(24.dp),
                                    painter = painterResource(Res.drawable.icon_plus),
                                    contentDescription = null,
                                    colorFilter = ColorFilter.tint(Color(0xFFA2A2A2)),
                                )
                                if (showAddPopup) {
                                    PickReadingPagePopup(
                                        pageRange = (latestPage + 1)..book.book.pageCount,
                                        onPick = { page ->
                                            onReadingEventAdd(page)
                                            showAddPopup = false
                                        },
                                        onDismissRequest = { showAddPopup = false }
                                    )
                                }
                            }
                        }

                    }
                )
            },
            content = {
                if (events.isEmpty()) {
                    Text(
                        text = stringResource(Res.string.collected_book_reading_progress_placeholder),
                        fontSize = 16.sp,
                        color = Color(0xFF7C7C7C),
                    )
                } else {
                    ReadingEvents(
                        events = events
                    )
                }
            }
        )
    }
}

@Composable
private fun ReadingEventsIcon(
    events: ImmutableList<CollectedBook.ReadingEvent>,
    pageCount: Int,
) {
    val progress = remember(events, pageCount) {
        val latestPage = events.maxOfOrNull { it.page } ?: 0
        latestPage.toFloat() / pageCount
    }
    val color = LocalContentColor.current
    Canvas(Modifier.size(24.dp)) {
        val outerRadius = 18.dp.toPx() / 2
        val innerRadius = 12.dp.toPx() / 2
        drawCircle(
            color = color,
            radius = outerRadius,
            style = Stroke(
                width = 2.dp.toPx()
            )
        )
        if (progress > 0f) {
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = progress * 360f,
                useCenter = true,
                topLeft = Offset(center.x - innerRadius, center.y - innerRadius),
                size = Size(innerRadius * 2, innerRadius * 2),
            )
        }
    }
}

@Composable
private fun ReadingEvents(
    events: ImmutableList<CollectedBook.ReadingEvent>,
) {
    val lazyListState = rememberLazyListState()
    LaunchedEffect(events.size) {
        lazyListState.animateScrollToItem(events.size)
    }
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        state = lazyListState,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Bottom,
    ) {
        items(events) { event ->
            PageItem(
                at = event.at,
                page = event.page
            )
        }
    }
}

@Composable
private fun PageItem(
    at: Instant,
    page: Int
) {
    Column(
        modifier = Modifier
            .height(56.dp)
            .widthIn(56.dp)
            .border(width = 1.dp, color = Color(0xFFDCDCDC), shape = RoundedCornerShape(16.dp))
            .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        val text = remember(at) {
            at.toLocalDateTime(TimeZone.currentSystemDefault()).date.format(
                LocalDate.Format {
                    monthNumber(padding = Padding.NONE)
                    chars("/")
                    dayOfMonth(padding = Padding.NONE)
                }
            )
        }
        Text(
            text = text,
            fontSize = 14.sp,
            color = Color(0xFFA2A2A2),
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
        Text(
            text = page.toString(),
            fontSize = 20.sp,
            fontFamily = RubikFontFamily(),
            color = Color(0xFF4A4A4A),
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
    }
}

@Composable
private fun PickReadingPagePopup(
    pageRange: IntRange,
    onPick: (Int) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val layoutDirection = LocalLayoutDirection.current
    val density = LocalDensity.current
    val offset = remember(density) {
        with(density) {
            val sign = if (layoutDirection == LayoutDirection.Ltr) 1f else -1f
            IntOffset(12.dp.roundToPx(), -12.dp.roundToPx()).times(sign)
        }
    }
    Popup(
        offset = offset,
        alignment = Alignment.TopEnd,
        onDismissRequest = onDismissRequest,
    ) {
        FloatButtonBox(padding = PaddingValues(start = 24.dp, end = 12.dp)) {
            Row {
                val pageRangeList = remember(pageRange) { pageRange.toImmutableList() }
                val lazyListState = rememberLazyListState()
                val currentPage by produceState(
                    pageRangeList.firstOrNull() ?: 0,
                    pageRangeList,
                    lazyListState
                ) {
                    snapshotFlow { lazyListState.layoutInfo }
                        .collectLatest { layoutInfo ->
                            layoutInfo.visibleItemsInfo
                                .minByOrNull { it.offset.absoluteValue }
                                ?.index
                                ?.coerceIn(pageRangeList.indices)
                                ?.let { value = pageRangeList[it] }
                        }
                }
                val listVerticalPadding = remember { 16.dp }
                val itemCountInViewport = 5
                val itemSpacing = remember { 12.dp }
                val itemHeight = remember { 24.dp }
                val listHeight = remember(itemHeight, itemCountInViewport) {
                    listVerticalPadding * 2 +
                            itemCountInViewport * itemHeight +
                            (itemCountInViewport - 1) * itemSpacing
                }
                val listBottomPadding = remember(listHeight, itemHeight) {
                    listHeight - itemHeight - itemSpacing
                }
                LazyColumn(
                    modifier = Modifier.height(listHeight),
                    state = lazyListState,
                    contentPadding = PaddingValues(
                        top = 16.dp,
                        bottom = listBottomPadding
                    ),
                    flingBehavior = rememberSnapFlingBehavior(
                        lazyListState = lazyListState,
                        snapPosition = SnapPosition.Start
                    ),
                    verticalArrangement = Arrangement.spacedBy(itemSpacing),
                ) {
                    items(pageRangeList) { page ->
                        val isSelected by remember { derivedStateOf { page == currentPage } }
                        Box(Modifier.height(itemHeight), contentAlignment = Alignment.TopCenter) {
                            Text(
                                text = page.toString(),
                                color = if (isSelected) Color(0xFF4B4B4B) else Color(0xFFC4C4C4),
                                fontSize = 20.sp,
                                fontWeight = if (isSelected) FontWeight.W500 else FontWeight.W400,
                            )
                        }
                    }
                }
                Spacer(Modifier.width(14.dp))
                Box(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .size(24.dp)
                        .background(Color(0xFF4B4B4B), shape = CircleShape)
                        .clickable { onPick(currentPage) },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(Res.drawable.icon_plus_small),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Color.White),
                    )
                }
            }
        }
    }
}