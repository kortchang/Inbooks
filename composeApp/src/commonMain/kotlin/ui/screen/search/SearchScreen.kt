package ui.screen.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.di.getViewModel
import book.composeapp.generated.resources.Res
import book.composeapp.generated.resources.icon_nav_arrow_left
import book.composeapp.generated.resources.search_book_placeholder
import domain.book.SearchedBook
import io.kamel.core.Resource
import io.kamel.image.asyncPainterResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ui.foundation.Shadow
import ui.foundation.shadow

@Composable
fun SearchScreen(
    back: () -> Unit,
    navigateToDetail: (SearchedBook) -> Unit,
) {
    val viewModel = getViewModel<SearchViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    Surface {
        Column(
            modifier = Modifier.fillMaxSize().systemBarsPadding()
                .padding(top = 12.dp),
        ) {
            Image(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable(onClick = back)
                    .minimumInteractiveComponentSize()
                    .size(24.dp),
                painter = painterResource(Res.drawable.icon_nav_arrow_left),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color(0xFF6F6F6F))
            )
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
            ) {
                var bottomPadding by remember { mutableStateOf(0.dp) }
                SearchedBooksList(
                    modifier = Modifier.matchParentSize(),
                    books = uiState.searchedBooks,
                    onBookClick = navigateToDetail,
                    contentPadding = PaddingValues(
                        top = 24.dp,
                        bottom = bottomPadding + 24.dp,
                    )
                )
                val density = LocalDensity.current
                Box(
                    Modifier.align(Alignment.BottomCenter)
                        .onSizeChanged { bottomPadding = with(density) { it.height.toDp() } }
                        .imePadding()
                        .padding(bottom = 24.dp)
                ) {
                    FloatButton(
                        isLoading = uiState.isLoading,
                        onTextChange = { uiState.intentTo(SearchUiIntent.Search(it)) }
                    )
                }
            }
        }
    }
}

@Composable
fun SearchedBooksList(
    modifier: Modifier = Modifier,
    books: List<SearchedBook>,
    onBookClick: (SearchedBook) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = contentPadding,
    ) {
        items(books) { book ->
            Book(
                book = book,
                onClick = { onBookClick(book) }
            )
        }
    }
}

@Composable
fun Book(
    modifier: Modifier = Modifier,
    book: SearchedBook,
    onClick: (() -> Unit)? = null,
    coverWidth: Dp = 72.dp,
) {
    Row(
        modifier = modifier
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(all = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Box(
            Modifier
                .shadow(
                    Shadow.Drop(
                        color = Color.Black.copy(alpha = 0.15f),
                        blur = 20.dp,
                        offsetX = (-4).dp,
                        offsetY = 4.dp
                    ),
                    shape = RoundedCornerShape(3.dp)
                )
        ) {
            val coverResource = asyncPainterResource(book.coverUrl ?: "")
            when (coverResource) {
                is Resource.Failure -> Unit
                is Resource.Loading -> Spacer(Modifier.width(coverWidth))
                is Resource.Success -> Image(
                    modifier = Modifier.width(coverWidth),
                    painter = coverResource.value,
                    contentDescription = book.name,
                    contentScale = ContentScale.FillWidth
                )

            }
            Row(Modifier.matchParentSize()) {
                Spacer(
                    Modifier
                        .fillMaxHeight()
                        .width(5.dp)
                        .alpha(0.2f)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF000000),
                                    Color(0xFF818181)
                                )
                            )
                        )
                )
                Spacer(
                    Modifier
                        .fillMaxHeight()
                        .width(3.dp)
                        .alpha(0.2f)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFFB7B7B7),
                                    Color(0xFF000000),
                                )
                            )
                        )
                )
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = book.name,
                fontSize = 16.sp,
                color = Color(0xFF2A2A2A),
                fontWeight = FontWeight.W600,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = book.authors.joinToString(" "),
                fontSize = 14.sp,
                color = Color(0xFFA0A0A0),
            )
        }
    }
}

@Composable
fun FloatButton(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    onTextChange: (String) -> Unit
) {
    var previousIsLoading by remember { mutableStateOf(isLoading) }
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val animatedWidthfraction by animateFloatAsState(if (isFocused) 1f else 0.8f)
    val focusManager = LocalFocusManager.current
    LaunchedEffect(isLoading, focusManager) {
        if (isLoading.not() && previousIsLoading) {
            focusManager.clearFocus()
        }
        previousIsLoading = isLoading
    }
    var textFieldValue: TextFieldValue by remember { mutableStateOf(TextFieldValue()) }
    BasicTextField(
        value = textFieldValue,
        onValueChange = {
            textFieldValue = it
            onTextChange(it.text)
        },
        textStyle = TextStyle(
            fontSize = 16.sp,
            color = Color(0xFF525252),
        ),
        interactionSource = interactionSource,
        singleLine = true,
        decorationBox = { innerTextField ->
            Row(
                modifier = modifier
                    .padding(horizontal = 24.dp)
                    .heightIn(min = 56.dp)
                    .fillMaxWidth(fraction = animatedWidthfraction)
                    .shadow(
                        Shadow.Drop(
                            Color.Black.copy(alpha = 0.04f),
                            blur = 20.dp,
                            offsetY = 5.dp
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .background(color = Color.White)
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Box(
                    modifier = Modifier.then(if (isFocused) Modifier.weight(1f) else Modifier),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        modifier = Modifier
                            .alpha(if (textFieldValue.text.isEmpty()) 1f else 0f),
                        text = stringResource(Res.string.search_book_placeholder),
                        color = Color(0xFF9A9A9A),
                        fontSize = 16.sp,
                    )
                    innerTextField()
                }
                Spacer(Modifier.width(16.dp))
                AnimatedVisibility(visible = isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(12.dp),
                        color = Color(0xFF9A9A9A),
                        strokeWidth = 1.dp
                    )
                }
            }
        }
    )
}