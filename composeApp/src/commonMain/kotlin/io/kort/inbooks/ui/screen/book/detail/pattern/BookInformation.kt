package io.kort.inbooks.ui.screen.book.detail.pattern

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.kort.inbooks.ui.resource.Res
import io.kort.inbooks.ui.resource.book_detail_category_title
import io.kort.inbooks.ui.resource.book_detail_description_title
import io.kort.inbooks.ui.resource.book_detail_published_date_title
import io.kort.inbooks.ui.resource.book_detail_publisher_title
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import io.kort.inbooks.domain.model.book.Book
import io.kort.inbooks.ui.token.system.System
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BookInformation(book: Book) {
    Column {
        var descriptionLoaded by remember(book.description) { mutableStateOf(book.description == null) }
        book.description?.let {
            Description(description = it, onLoad = { descriptionLoaded = true })
        }
        val items = remember(book) {
            listOfNotNull(
                book.publishedDate?.let { Res.string.book_detail_published_date_title to it },
                book.publisher?.let { Res.string.book_detail_publisher_title to it },
                book.categories?.firstOrNull()?.let { Res.string.book_detail_category_title to it }
            )
        }
        if (descriptionLoaded && items.isNotEmpty()) {
            Spacer(Modifier.height(32.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(48.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                maxItemsInEachRow = 3,
            ) {
                items.forEach { (titleRes, value) ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Subtitle(text = stringResource(titleRes))
                        Text(
                            text = value,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W600,
                            color = System.colors.onBackground,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Subtitle(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = 16.sp,
        color = System.colors.onBackgroundVariant,
    )
}

@Composable
private fun Description(
    description: String,
    onLoad: () -> Unit,
) {
    val richTextState = rememberRichTextState()
    var isLoad by remember(description) { mutableStateOf(false) }
    LaunchedEffect(description) {
        richTextState.setHtml(description)
        isLoad = true
        onLoad()
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Subtitle(
            text = stringResource(Res.string.book_detail_description_title)
        )
        RichText(
            modifier = Modifier.fillMaxWidth(),
            state = richTextState,
            fontSize = 16.sp,
            color = Color(0xFF4A4A4A),
            overflow = TextOverflow.Ellipsis,
        )
    }
}