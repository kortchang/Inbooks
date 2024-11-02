package io.kort.inbooks.ui.screen.search.addbylink

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import io.kort.inbooks.app.di.getViewModel
import io.kort.inbooks.domain.model.book.Book
import io.kort.inbooks.domain.model.book.BookId
import io.kort.inbooks.ui.component.Button
import io.kort.inbooks.ui.component.ButtonDefaults
import io.kort.inbooks.ui.component.TextField
import io.kort.inbooks.ui.pattern.book.BookCover
import io.kort.inbooks.ui.pattern.book.BookDetail
import io.kort.inbooks.ui.pattern.book.BookTitle
import io.kort.inbooks.ui.resource.*
import io.kort.inbooks.ui.resource.Res
import io.kort.inbooks.ui.resource.search_book_add_by_link_books_tw_analyzing
import io.kort.inbooks.ui.resource.search_book_add_by_link_books_tw_error
import io.kort.inbooks.ui.resource.search_book_add_by_link_books_tw_placeholder
import io.kort.inbooks.ui.resource.search_book_add_by_link_books_tw_reenter
import io.kort.inbooks.ui.resource.search_book_add_by_link_books_tw_title
import io.kort.inbooks.ui.resource.searched_book_detail_collect
import io.kort.inbooks.ui.token.system.System
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.datetime.Clock
import org.jetbrains.compose.resources.stringResource
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddByLinkSheet(
    onDismissRequest: () -> Unit,
    navigateToCollectedBookDetail: (BookId) -> Unit,
) {
    val viewModelStoreOwner = remember {
        object : ViewModelStoreOwner {
            override val viewModelStore: ViewModelStore = ViewModelStore()
            fun dispose() = viewModelStore.clear()
        }
    }

    ModalBottomSheet(
        onDismissRequest = {
            viewModelStoreOwner.dispose()
            onDismissRequest()
        },
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = System.colors.background,
        contentColor = System.colors.onBackground,
        dragHandle = { BottomSheetDefaults.DragHandle(color = System.colors.onBackgroundVariant) },
    ) {
        val viewModel = getViewModel<AddByLinkViewModel>(
            viewModelStoreOwner = viewModelStoreOwner,
        )

        val updatedNavigateToCollectedBookDetail by rememberUpdatedState(navigateToCollectedBookDetail)
        var url by remember { mutableStateOf(TextFieldValue()) }
        LaunchedEffect(viewModel.uiEvent) {
            viewModel.uiEvent.collectLatest { event ->
                when (event) {
                    is AddByLinkUiEvent.Collected -> updatedNavigateToCollectedBookDetail(event.collectedBookId)
                    is AddByLinkUiEvent.ClearUrl -> url = TextFieldValue()
                }
            }
        }

        val uiState by viewModel.uiState.collectAsState()
        val layoutDirection = LocalLayoutDirection.current
        Content(
            modifier = Modifier
                .padding(bottom = System.spacing.pagePadding.calculateBottomPadding())
                .padding(
                    start = System.spacing.pagePadding.calculateStartPadding(layoutDirection),
                    end = System.spacing.pagePadding.calculateEndPadding(layoutDirection)
                ),
            uiState = uiState,
            url = url,
            onUrlChange = { url = it },
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    uiState: AddByLinkUiState,
    url: TextFieldValue,
    onUrlChange: (TextFieldValue) -> Unit,
) {
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        Title(modifier = Modifier.fillMaxWidth())
        SubContent(uiState = uiState, url = url, onUrlChange = onUrlChange)
        MainButton(uiState = uiState, link = url.text)
    }
}

@Composable
private fun Title(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
    ) {
        Icon(imageVector = Icons.Language, contentDescription = null)
        Text(
            text = stringResource(Res.string.search_book_add_by_link_books_tw_title),
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun SubContent(
    uiState: AddByLinkUiState,
    url: TextFieldValue,
    onUrlChange: (TextFieldValue) -> Unit,
) {
    when (uiState) {
        is AddByLinkUiState.Entering -> {
            TextField(
                value = url,
                onValueChange = onUrlChange,
                placeholder = stringResource(Res.string.search_book_add_by_link_books_tw_placeholder),
                end = {
                    val clipboardManager = LocalClipboardManager.current
                    if (clipboardManager.hasText()) {
                        Icon(
                            imageVector = Icons.PasteClipboard,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                val textFromClipboard = clipboardManager.getText()
                                if (textFromClipboard != null) {
                                    onUrlChange(TextFieldValue(textFromClipboard))
                                }
                            }
                        )
                    }
                }
            )
        }

        is AddByLinkUiState.Analyzing -> {
            AnalyzingSubContent()
        }

        is AddByLinkUiState.AnalyzedFailed -> {
            AnalyzedFailedSubContent()
        }

        is AddByLinkUiState.AnalyzedSuccess -> {
            AnalyzedSuccessSubContent(uiState.book)
        }
    }
}

@Composable
private fun AnalyzingSubContent(
    modifier: Modifier = Modifier
) {
    val color = System.colors.onBackground
    val progressTransition = rememberInfiniteTransition()
    val progress by progressTransition.animateFloat(
        initialValue = -0.3f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(tween(durationMillis = 2000))
    )
    Row(
        modifier = modifier.fillMaxWidth()
            .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
            .drawWithContent {
                drawContent()
                drawRect(
                    brush = Brush.linearGradient(
                        listOf(
                            color.copy(alpha = 0.3f),
                            color,
                            color.copy(alpha = 0.3f),
                        ),
                        start = Offset(x = size.width * progress, y = 0f),
                        end = Offset(x = size.width * progress + 50.dp.toPx(), y = 50.dp.toPx()),
                    ),
                    blendMode = BlendMode.SrcIn
                )
            },
        horizontalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.CenterHorizontally)
    ) {
        BookPlaceholder(fillColor = Color.Transparent, strokeColor = Color.Black)
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            repeat(2) { parentIndex ->
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    repeat(2) { childIndex ->
                        val animatable = remember { androidx.compose.animation.core.Animatable(initialValue = 0f) }
                        LaunchedEffect(Unit) {
                            delay((parentIndex + childIndex) * 150L)
                            animatable.animateTo(1f, animationSpec = spring(stiffness = 1100f))
                        }
                        val scale by animatable.asState()
                        val widthRatio =
                            remember(parentIndex, childIndex) {
                                val seed = Clock.System.now().toEpochMilliseconds() + parentIndex + childIndex
                                Random(seed).nextInt(if (childIndex == 0) 80 else 10, 100) / 100f
                            }
                        Box(
                            Modifier
                                .graphicsLayer {
                                    scaleX = scale
                                    transformOrigin = TransformOrigin(0f, 0f)
                                }
                                .fillMaxWidth(widthRatio)
                                .height(10.dp)
                                .background(color = Color.Black, CircleShape)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AnalyzedFailedSubContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        val color = System.colors.warning
        Box(contentAlignment = Alignment.Center) {
            BookPlaceholder(
                fillColor = color.copy(alpha = 0.1f),
                strokeColor = color
            )
            Icon(
                imageVector = Icons.Xmark,
                contentDescription = null,
                tint = color,
            )
        }
        Text(
            text = stringResource(Res.string.search_book_add_by_link_books_tw_error),
            color = color,
            fontSize = 16.sp,
            lineHeight = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun AnalyzedSuccessSubContent(book: Book) {
    BookDetail(
        modifier = Modifier.fillMaxWidth(),
        cover = { BookCover(book) },
        title = { BookTitle(book) },
    )
}

@Composable
private fun BookPlaceholder(
    modifier: Modifier = Modifier,
    fillColor: Color,
    strokeColor: Color
) {
    Box(
        modifier.size(80.dp, 110.dp)
            .drawWithCache {
                onDrawBehind {
                    drawRoundRect(
                        color = fillColor,
                        cornerRadius = CornerRadius(8.dp.toPx()),
                    )

                    val strokeWidth = 1.5.dp.toPx()
                    val haltStrokeWidth = strokeWidth / 2f
                    drawRoundRect(
                        topLeft = Offset(x = haltStrokeWidth, y = haltStrokeWidth),
                        size = size.copy(size.width - strokeWidth, size.height - strokeWidth),
                        color = strokeColor,
                        style = Stroke(
                            width = strokeWidth,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(5.dp.toPx(), 10.dp.toPx()))
                        ),
                        cornerRadius = CornerRadius(8.dp.toPx()),
                    )
                }
            }
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun MainButton(uiState: AddByLinkUiState, link: String) {
    SharedTransitionLayout {
        AnimatedContent(uiState) {
            val buttonModifier = Modifier.sharedElement(
                state = rememberSharedContentState("button"),
                animatedVisibilityScope = this,
            )

            when (it) {
                is AddByLinkUiState.Entering -> {
                    Button(
                        modifier = buttonModifier.fillMaxWidth(),
                        enabled = link.isNotBlank(),
                        colors = ButtonDefaults.secondaryButtonColors(),
                        onClick = { uiState.intentTo(AddByLinkUiIntent.Analyze(link)) },
                        start = { Icon(Icons.Plus, contentDescription = null) },
                        text = { Text(stringResource(Res.string.searched_book_detail_collect)) }
                    )
                }

                is AddByLinkUiState.Analyzing -> {
                    Button(
                        modifier = buttonModifier.fillMaxWidth(),
                        enabled = false,
                        colors = ButtonDefaults.secondaryButtonColors(),
                        onClick = {},
                        text = { Text(stringResource(Res.string.search_book_add_by_link_books_tw_analyzing)) }
                    )
                }

                is AddByLinkUiState.AnalyzedFailed -> {
                    Button(
                        modifier = buttonModifier.fillMaxWidth(),
                        colors = ButtonDefaults.secondaryButtonColors(),
                        onClick = { uiState.intentTo(AddByLinkUiIntent.BackToEnter) },
                        start = { Icon(Icons.Undo, contentDescription = null) },
                        text = { Text(stringResource(Res.string.search_book_add_by_link_books_tw_reenter)) }
                    )
                }

                is AddByLinkUiState.AnalyzedSuccess -> {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            colors = ButtonDefaults.outlineButtonColors(),
                            onClick = { uiState.intentTo(AddByLinkUiIntent.BackToEnter) },
                            start = { Icon(Icons.NavArrowLeft, contentDescription = null) },
                        )
                        Button(
                            modifier = buttonModifier.weight(1f),
                            colors = ButtonDefaults.secondaryButtonColors(),
                            onClick = { uiState.intentTo(AddByLinkUiIntent.Collect) },
                            start = { Icon(Icons.Plus, contentDescription = null) },
                            text = { Text(stringResource(Res.string.searched_book_detail_collect)) }
                        )
                    }
                }
            }
        }
    }
}
