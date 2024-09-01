@file:OptIn(ExperimentalSharedTransitionApi::class)

package ui.feature.search

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.di.getViewModel
import book.composeapp.generated.resources.Res
import book.composeapp.generated.resources.icon_nav_arrow_left
import book.composeapp.generated.resources.search_book_placeholder
import domain.book.SearchedBook
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ui.component.BookWithInformation
import ui.component.FloatButtonBox

@Composable
fun SearchScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
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
                    .offset(x = 16.dp)
                    .size(48.dp)
                    .clickable(onClick = back)
                    .wrapContentSize(align = Alignment.Center)
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
                    ),
                    sharedTransitionScope = sharedTransitionScope,
                    animatedContentScope = animatedContentScope,
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
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedContentScope: AnimatedContentScope? = null,
) {
    val state = rememberLazyListState()
    val focusManager = LocalFocusManager.current
    LaunchedEffect(state.isScrollInProgress, focusManager) {
        if (state.isScrollInProgress) {
            focusManager.clearFocus()
        }
    }
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(horizontal = 16.dp),
        state = state,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = contentPadding,
    ) {
        items(books) { book ->
            BookWithInformation(
                book = book.book,
                onClick = { onBookClick(book) },
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope,
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
    var textFieldValue: TextFieldValue by remember { mutableStateOf(TextFieldValue()) }
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val animatedWidthFraction by animateFloatAsState(if (isFocused) 1f else 0.8f)
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        if (textFieldValue.text.isEmpty()) {
            focusRequester.requestFocus()
        }
    }
    BasicTextField(
        modifier = Modifier.focusRequester(focusRequester),
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
            FloatButtonBox(
                modifier = modifier
                    .padding(horizontal = 24.dp)
                    .heightIn(min = 56.dp)
                    .fillMaxWidth(fraction = animatedWidthFraction),
                contentAlignment = Alignment.CenterStart,
            ) {
                Row(
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
        }
    )
}