package io.kort.inbooks.ui.screen.topic.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.kort.inbooks.ui.resource.Res
import io.kort.inbooks.ui.resource.illustration_empty_topic_list
import io.kort.inbooks.ui.resource.topic_list_empty_description
import io.kort.inbooks.ui.resource.topic_list_empty_title
import io.kort.inbooks.ui.resource.topic_list_title
import io.kort.inbooks.app.di.getViewModel
import io.kort.inbooks.domain.model.topic.Topic
import io.kort.inbooks.ui.component.ButtonDefaults
import io.kort.inbooks.ui.component.IconButton
import io.kort.inbooks.ui.component.PageScope
import io.kort.inbooks.ui.foundation.plus
import io.kort.inbooks.ui.pattern.Empty
import io.kort.inbooks.ui.pattern.topic.TopicDetail
import io.kort.inbooks.ui.resource.Icons
import io.kort.inbooks.ui.resource.Plus
import io.kort.inbooks.ui.token.system.System
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun PageScope.TopicListScreen(
    navigateToAddTopic: () -> Unit,
    navigateToTopicDetail: (topic: Topic) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TopicListViewModel = getViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    Column(modifier.windowInsetsPadding(excludeBottomWindowInsets)) {
        TopAppBar(onAdd = navigateToAddTopic)
        (uiState as? TopicListUiState.Initialized)?.let { state ->
            if (state.topics.isEmpty()) {
                Empty(
                    modifier = Modifier.weight(1f).windowInsetsPadding(bottomWindowInsets),
                    illustration = painterResource(Res.drawable.illustration_empty_topic_list),
                    title = stringResource(Res.string.topic_list_empty_title),
                    description = stringResource(Res.string.topic_list_empty_description),
                )
            } else {
                Spacer(Modifier.height(32.dp))
                List(
                    modifier = Modifier.weight(1f),
                    windowInsets = bottomWindowInsets,
                    topics = state.topics,
                    onTopicClick = navigateToTopicDetail
                )
            }
        }
    }
}

@Composable
private fun TopAppBar(
    onAdd: () -> Unit
) {
    io.kort.inbooks.ui.component.TopAppBar(
        start = {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(Res.string.topic_list_title),
                fontSize = 20.sp,
                fontWeight = FontWeight.W600,
                color = System.colors.onSurface,
            )
        },
        end = {
            IconButton(
                icon = Icons.Plus,
                onClick = onAdd,
                colors = ButtonDefaults.colorsOfSecondary()
            )
        }
    )
}

@Composable
private fun List(
    topics: List<Topic>,
    windowInsets: WindowInsets,
    onTopicClick: (topic: Topic) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = windowInsets.asPaddingValues() + PaddingValues(top = 32.dp),
    ) {
        items(topics) { topic ->
            TopicDetail(
                topic = topic,
                onClick = { onTopicClick(topic) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}