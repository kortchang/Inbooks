package io.kort.inbooks.ui.screen.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.kort.inbooks.ui.resource.Res
import io.kort.inbooks.ui.resource.dashboard_book_title
import io.kort.inbooks.ui.resource.dashboard_empty_description
import io.kort.inbooks.ui.resource.dashboard_empty_title
import io.kort.inbooks.ui.resource.dashboard_topic_title
import io.kort.inbooks.ui.resource.illustration_empty_dashboard
import io.kort.inbooks.ui.resource.see_all
import io.kort.inbooks.app.di.getViewModel
import io.kort.inbooks.domain.model.book.CollectedBook
import io.kort.inbooks.domain.model.topic.Topic
import io.kort.inbooks.ui.component.Avatar
import io.kort.inbooks.ui.component.AvatarDefaults.redDot
import io.kort.inbooks.ui.component.PageScope
import io.kort.inbooks.ui.component.TopAppBar
import io.kort.inbooks.ui.foundation.SharedScene
import io.kort.inbooks.ui.foundation.plus
import io.kort.inbooks.ui.foundation.thenIf
import io.kort.inbooks.ui.pattern.Empty
import io.kort.inbooks.ui.pattern.book.BookCover
import io.kort.inbooks.ui.pattern.book.BookCoverDefaults
import io.kort.inbooks.ui.pattern.book.BookCoverLayoutStyle
import io.kort.inbooks.ui.pattern.topic.TopicDetail
import io.kort.inbooks.ui.screen.dashboard.signup.SignUpOnboardingSheet
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun PageScope.DashboardScreen(
    navigateToBookDetail: (collectedBook: CollectedBook) -> Unit,
    navigateToTopic: (topic: Topic) -> Unit,
    navigateToBookList: () -> Unit,
    navigateToTopicList: () -> Unit,
    navigateToSignUp: () -> Unit,
    navigateToSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = getViewModel<DashboardViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    (uiState as? DashboardUiState.Initialized)?.let { loadedUiState ->
        Content(
            modifier = modifier.fillMaxSize(),
            windowInsets = windowInsets,
            uiState = loadedUiState,
            navigateToBookDetail = navigateToBookDetail,
            navigateToTopic = navigateToTopic,
            navigateToBookCollection = navigateToBookList,
            navigateToTopicCollection = navigateToTopicList,
            navigateToSignUp = navigateToSignUp,
            navigateToSettings = navigateToSettings,
        )
    }
}

@Composable
private fun Content(
    windowInsets: WindowInsets,
    uiState: DashboardUiState.Initialized,
    navigateToBookDetail: (collectedBook: CollectedBook) -> Unit,
    navigateToTopic: (topic: Topic) -> Unit,
    navigateToBookCollection: () -> Unit,
    navigateToTopicCollection: () -> Unit,
    navigateToSignUp: () -> Unit,
    navigateToSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val horizontalWindowInsets = windowInsets.only(WindowInsetsSides.Horizontal)
    val topWindowInsets = windowInsets.only(WindowInsetsSides.Top)
    val bottomWindowInsets = windowInsets.only(WindowInsetsSides.Bottom)
    Column(
        modifier = modifier.fillMaxSize().windowInsetsPadding(topWindowInsets),
    ) {
        TopAppBar(horizontalWindowInsets, uiState, navigateToSettings, navigateToSignUp)
        if (uiState.books.isEmpty() && uiState.topics.isEmpty()) {
            Empty(
                modifier = Modifier.weight(1f).windowInsetsPadding(horizontalWindowInsets.union(bottomWindowInsets)),
                illustration = painterResource(Res.drawable.illustration_empty_dashboard),
                title = stringResource(Res.string.dashboard_empty_title),
                description = stringResource(Res.string.dashboard_empty_description),
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = bottomWindowInsets.asPaddingValues() + PaddingValues(top = 64.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                books(
                    horizontalWindowInsets = horizontalWindowInsets,
                    books = uiState.books,
                    navigateToBookCollection = navigateToBookCollection,
                    navigateToBookDetail = navigateToBookDetail
                )

                item { Spacer(Modifier.height(64.dp)) }

                topics(
                    horizontalWindowInsets = horizontalWindowInsets,
                    topics = uiState.topics,
                    navigateToTopicCollection = navigateToTopicCollection,
                    navigateToTopic = navigateToTopic
                )
            }
        }
    }
}

@Composable
private fun TopAppBar(
    horizontalWindowInsets: WindowInsets,
    uiState: DashboardUiState.Initialized,
    navigateToSettings: () -> Unit,
    navigateToSignUp: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.windowInsetsPadding(horizontalWindowInsets),
        start = {
            var showSignUpOnboardingSheet by remember { mutableStateOf(false) }
            Avatar(
                modifier = Modifier.thenIf(uiState.showRedDotOnAvatarToOnboardSignUp) { redDot() },
                url = null,
                onClick = {
                    if (uiState.isLoggedIn) {
                        navigateToSettings()
                    } else {
                        showSignUpOnboardingSheet = true
                    }
                }
            )

            if (showSignUpOnboardingSheet) {
                SignUpOnboardingSheet(
                    onDismissRequest = {
                        showSignUpOnboardingSheet = false
                        uiState.intentTo(DashboardUiIntent.MarkIsOnboardedSignUp)
                    },
                    navigateToSignUp = navigateToSignUp,
                )
            }
        }
    )
}

private fun LazyListScope.books(
    horizontalWindowInsets: WindowInsets,
    books: List<CollectedBook>,
    navigateToBookCollection: () -> Unit,
    navigateToBookDetail: (collectedBook: CollectedBook) -> Unit
) {
    if (books.isNotEmpty()) {
        item {
            DefaultSectionContainer(
                modifier = Modifier.windowInsetsPadding(horizontalWindowInsets),
                title = stringResource(Res.string.dashboard_book_title),
                onSeeAllClick = navigateToBookCollection
            )
        }

        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = horizontalWindowInsets.asPaddingValues(),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                items(
                    items = books,
                    key = { book -> book.book.id }
                ) { book ->
                    BookCover(
                        modifier = Modifier.animateItem(placementSpec = null),
                        book = book.book,
                        onClick = { navigateToBookDetail(book) },
                        layout = BookCoverDefaults.layout(fillBy = BookCoverLayoutStyle.FillBy.Height.Medium),
                        share = BookCoverDefaults.share(listOf(SharedScene.DashboardAndBookDetail)),
                    )
                }
            }
        }
    }
}

private fun LazyListScope.topics(
    horizontalWindowInsets: WindowInsets,
    topics: List<Topic>,
    navigateToTopicCollection: () -> Unit,
    navigateToTopic: (topic: Topic) -> Unit
) {
    if (topics.isNotEmpty()) {
        item {
            DefaultSectionContainer(
                modifier = Modifier.windowInsetsPadding(horizontalWindowInsets),
                title = stringResource(Res.string.dashboard_topic_title),
                onSeeAllClick = navigateToTopicCollection
            )
        }

        items(topics) { topic ->
            TopicDetail(
                modifier = Modifier.windowInsetsPadding(horizontalWindowInsets).animateItem(placementSpec = null),
                topic = topic,
                onClick = { navigateToTopic(topic) }
            )
        }
    }
}

@Composable
private fun DefaultSectionContainer(
    title: String,
    onSeeAllClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier.fillMaxWidth().padding(bottom = 16.dp)) {
        Text(
            modifier = Modifier.weight(1f),
            text = title,
            fontSize = 16.sp,
            color = io.kort.inbooks.ui.token.system.System.colors.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            modifier = Modifier.clickable(onClick = onSeeAllClick),
            text = stringResource(Res.string.see_all),
            fontSize = 14.sp,
            color = io.kort.inbooks.ui.token.system.System.colors.onSurfaceVariant,
        )
    }
}