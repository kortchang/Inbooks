package ui.feature.bookdetail.pattern

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ContextualFlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
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
import book.composeapp.generated.resources.Res
import book.composeapp.generated.resources.book_detail_book_information_title
import book.composeapp.generated.resources.book_detail_category_title
import book.composeapp.generated.resources.book_detail_description_title
import book.composeapp.generated.resources.book_detail_published_date_title
import book.composeapp.generated.resources.book_detail_publisher_title
import book.composeapp.generated.resources.icon_google_docs
import book.composeapp.generated.resources.icon_minus
import book.composeapp.generated.resources.icon_nav_arrow_down
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import domain.book.Book
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class)
fun LazyListScope.bookInformation(book: Book) {
    item(key = "information") {
        var expand by remember { mutableStateOf(false) }
        DetailSection(
            title = {
                DetailSectionTitle(
                    modifier = Modifier.clickable { expand = !expand },
                    icon = painterResource(Res.drawable.icon_google_docs),
                    title = stringResource(Res.string.book_detail_book_information_title),
                    end = {
                        AnimatedContent(expand) {
                            val painter =
                                painterResource(if (it) Res.drawable.icon_minus else Res.drawable.icon_nav_arrow_down)
                            Icon(
                                painter = painter,
                                contentDescription = null,
                            )
                        }
                    }
                )

            }
        ) {
            book.description?.let { Description(it, expand = expand) }
            val items = remember(book) {
                listOfNotNull(
                    book.publishedDate?.let { Res.string.book_detail_published_date_title to it },
                    book.publisher?.let { Res.string.book_detail_publisher_title to it },
                    book.categories?.firstOrNull()?.let { Res.string.book_detail_category_title to it }
                )
            }
            Spacer(Modifier.height(36.dp))
            if (items.isNotEmpty()) {
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
                                color = Color(0xFF4A4A4A),
                            )
                        }
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
        color = Color(0xFFA2A2A2),
    )
}

@Composable
private fun Description(description: String, expand: Boolean, maxLinesInCollapsed: Int = 5) {
    val richTextState = rememberRichTextState()
    LaunchedEffect(description) {
        richTextState.setHtml(description)
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
            maxLines = if (expand) Int.MAX_VALUE else maxLinesInCollapsed,
            overflow = TextOverflow.Ellipsis,
        )
    }
}