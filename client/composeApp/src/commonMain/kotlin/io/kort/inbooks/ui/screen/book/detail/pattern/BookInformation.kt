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
import androidx.compose.runtime.rememberUpdatedState
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
import io.kort.inbooks.data.source.book.BookLocalModel
import io.kort.inbooks.domain.model.book.Book
import io.kort.inbooks.ui.token.system.System
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BookInformation(
    book: Book,
    colors: BookInformationColors,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        var descriptionLoaded by remember(book.description) { mutableStateOf(book.description == null) }
        book.description?.let {
            Description(
                description = it,
                colors = colors,
                onLoad = { descriptionLoaded = true }
            )
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
                        Subtitle(text = stringResource(titleRes), color = colors.onColorVariant)
                        Text(
                            text = value,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = colors.onColor,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Subtitle(
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = 16.sp,
        color = color,
    )
}

@Composable
private fun Description(
    description: String,
    colors: BookInformationColors,
    onLoad: () -> Unit,
) {
    val richTextState = rememberRichTextState()
    var isLoad by remember(description) { mutableStateOf(false) }
    val updatedOnLoad by rememberUpdatedState(onLoad)
    LaunchedEffect(description) {
        richTextState.setHtml(description)
        isLoad = true
        updatedOnLoad()
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Subtitle(
            text = stringResource(Res.string.book_detail_description_title),
            color = colors.onColorVariant,
        )
        RichText(
            modifier = Modifier.fillMaxWidth(),
            state = richTextState,
            fontSize = 16.sp,
            color = colors.onColor,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun DataSource(
    source: BookLocalModel.BasicBookLocalModel.Source,
    externalIds: List<Book.ExternalId>,
    colors: BookInformationColors,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Subtitle(
            text = stringResource(Res.string.book_detail_description_title),
            color = colors.onColorVariant,
        )

//        Text()
    }
}

data class BookInformationColors(
    val onColor: Color,
    val onColorVariant: Color,
)

object BookInformationDefaults {
    @Composable
    fun colorsOnBackground(): BookInformationColors = BookInformationColors(
        onColor = System.colors.onBackground,
        onColorVariant = System.colors.onBackgroundVariant,
    )

    @Composable
    fun colorsOnSurface(): BookInformationColors = BookInformationColors(
        onColor = System.colors.onSurface,
        onColorVariant = System.colors.onSurfaceVariant,
    )
}