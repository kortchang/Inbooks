@file:OptIn(ExperimentalSharedTransitionApi::class)

package ui.feature.dashboard

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.di.getViewModel
import book.composeapp.generated.resources.Res
import book.composeapp.generated.resources.dashboard_greeting
import book.composeapp.generated.resources.dashboard_recent_collect_title
import book.composeapp.generated.resources.dashboard_recent_read_title
import book.composeapp.generated.resources.dashboard_recent_reading_title
import book.composeapp.generated.resources.icon_collage_frame
import book.composeapp.generated.resources.icon_eye_solid
import domain.book.CollectedBook
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ui.component.Avatar
import ui.component.BookCover
import ui.component.BookCoverDefaults
import ui.component.BookCoverStyle
import ui.component.BookWithInformation
import ui.component.TopAppBar
import ui.component.rememberBookCoverColor
import ui.pattern.bottomAppBar
import ui.token.system.AppTheme

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DashboardScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    navigateToSettings: () -> Unit,
    navigateToBookDetail: (collectedBook: CollectedBook) -> Unit,
) {
    val viewModel = getViewModel<DashboardViewModel>()
    Box(Modifier.fillMaxSize()) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        (uiState as? DashboardUiState.Initialized)?.let { loadedUiState ->
            Content(
                modifier = Modifier.fillMaxSize(),
                uiState = loadedUiState,
                navigateToSettings = navigateToSettings,
                navigateToBookDetail = navigateToBookDetail,
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope,
            )
        }
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    uiState: DashboardUiState.Initialized,
    navigateToSettings: () -> Unit,
    navigateToBookDetail: (collectedBook: CollectedBook) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(64.dp),
    ) {
        DashboardTopAppBar(
            avatarUrl = uiState.avatarUrl,
            displayName = uiState.displayName,
            onAvatarClick = navigateToSettings
        )
        LazyColumn(
            modifier = modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(64.dp),
            contentPadding = WindowInsets.bottomAppBar.asPaddingValues(),
        ) {
            commonBooks(
                key = "recent_collected",
                books = uiState.recentCollectedBooks,
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope,
                titleText = { stringResource(Res.string.dashboard_recent_collect_title) },
                titleIcon = { painterResource(Res.drawable.icon_collage_frame) },
                navigateToBookDetail = navigateToBookDetail,
            )
        }
    }
}

//private fun LazyListScope.readingBooks(
//    books: ImmutableList<CollectedBook>,
//    sharedTransitionScope: SharedTransitionScope,
//    animatedContentScope: AnimatedContentScope,
//    navigateToBookDetail: (collectedBook: CollectedBook) -> Unit
//) {
//    commonBooks(
//        key = "recent_reading",
//        sharedTransitionScope = sharedTransitionScope,
//        animatedContentScope = animatedContentScope,
//        books = books,
//        titleText = { stringResource(Res.string.dashboard_recent_reading_title) },
//        titleColor = {
//            val firstBook = remember(books) { books.first() }
//            rememberBookCoverColor(firstBook.book.coverUrl ?: "").color
//        },
//        titleIcon = { null },
//        navigateToBookDetail = navigateToBookDetail,
//        content = {
//            Column(Modifier.fillMaxWidth()) {
//                val firstBook = remember(books) { books.first() }
//                val booksExceptFirst = remember(books) { books.drop(1).toPersistentList() }
//                BookWithInformation(
//                    book = firstBook.book,
//                    onClick = { navigateToBookDetail(firstBook) },
//                    padding = PaddingValues(horizontal = 32.dp),
//                    bookCoverStyle = BookCoverDefaults.layout(
//                        width = BookCoverStyle.Width.Medium,
//                        shadowColor = BookCoverStyle.ShadowColor.Cover,
//                    )
//                )
//                if (firstBook.readingReason.isNotBlank()) {
//                    Spacer(Modifier.height(24.dp))
//                    Text(
//                        modifier = Modifier.weight(1f),
//                        text = firstBook.readingReason,
//                    )
//                }
//                Spacer(Modifier.height(32.dp))
//                if (booksExceptFirst.isNotEmpty()) {
//                    LazyRow(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.spacedBy(20.dp),
//                        contentPadding = PaddingValues(horizontal = 32.dp)
//                    ) {
//                        items(
//                            key = { it.book.id },
//                            items = booksExceptFirst
//                        ) { book ->
//                            BookCover(
//                                book = book.book,
//                                onClick = { navigateToBookDetail(book) },
//                                sharedTransitionScope = sharedTransitionScope,
//                                animatedContentScope = animatedContentScope,
//                                style = BookCoverDefaults.layout(
//                                    shadowColor = BookCoverStyle.ShadowColor.Cover
//                                )
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    )
//}

private fun LazyListScope.commonBooks(
    key: String,
    books: ImmutableList<CollectedBook>,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    titleText: @Composable () -> String,
    titleColor: @Composable () -> Color = { Color(0xFF5F5F5F) },
    titleIcon: @Composable () -> Painter?,
    navigateToBookDetail: (collectedBook: CollectedBook) -> Unit,
    content: @Composable () -> Unit = {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(horizontal = 32.dp)
        ) {
            items(books) { book ->
                BookCover(
                    book = book.book,
                    onClick = { navigateToBookDetail(book) },
                    sharedTransitionScope = sharedTransitionScope,
                    animatedContentScope = animatedContentScope,
                    style = BookCoverDefaults.layout(
                        shadowColor = BookCoverStyle.ShadowColor.Environment
                    )
                )
            }
        }
    }
) {
    if (books.isNotEmpty()) {
        item(key) {
            Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                Row(
                    modifier = Modifier.padding(horizontal = 32.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    titleIcon()?.let {
                        Image(
                            painter = it,
                            contentDescription = titleText(),
                            colorFilter = ColorFilter.tint(Color(0xFF5F5F5F)),
                        )
                    }
                    Text(
                        modifier = Modifier.weight(1f),
                        text = titleText(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.W600,
                        color = titleColor(),
                    )
                }
                content()
            }
        }
    }
}

@Composable
private fun DashboardTopAppBar(
    avatarUrl: String,
    displayName: String,
    onAvatarClick: () -> Unit,
) {
    TopAppBar(
        start = {
            Avatar(
                url = avatarUrl,
                onClick = onAvatarClick,
            )
        },
        center = {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = displayName,
                    maxLines = 1,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W600,
                    color = AppTheme.Colors.onSurface,
                )
                Text(
                    text = stringResource(Res.string.dashboard_greeting),
                    maxLines = 1,
                    fontSize = 16.sp,
                    color = AppTheme.Colors.onSurfaceVariant,
                )
            }
        }
    )
}