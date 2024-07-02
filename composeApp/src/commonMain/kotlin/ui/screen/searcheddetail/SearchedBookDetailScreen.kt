package ui.screen.searcheddetail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.di.getViewModel
import book.composeapp.generated.resources.Res
import book.composeapp.generated.resources.icon_nav_arrow_down
import book.composeapp.generated.resources.icon_nav_arrow_left
import book.composeapp.generated.resources.searched_book_detail_description
import book.composeapp.generated.resources.searched_book_detail_expend_description
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import domain.book.SearchedBook
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.core.parameter.parametersOf
import ui.screen.search.Book

@Composable
fun SearchedBookDetailScreen(
    id: String,
    back: () -> Unit,
) {
    Surface {
        val viewModel = getViewModel<SearchedBookDetailViewModel> {
            parametersOf(id)
        }
        val uiState by viewModel.uiState.collectAsState()
        val bookOrNull = uiState.book
        Column(
            Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            Image(
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable(onClick = back)
                    .minimumInteractiveComponentSize()
                    .size(24.dp),
                painter = painterResource(Res.drawable.icon_nav_arrow_left),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color(0xFF6F6F6F))
            )

            bookOrNull?.let { book -> Content(book) }
        }
    }
}

@Composable
private fun Content(book: SearchedBook) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        Spacer(Modifier.height(36.dp))
        Book(
            modifier = Modifier.padding(horizontal = 20.dp),
            book = book,
            coverWidth = 103.dp
        )
        Spacer(Modifier.height(36.dp))
        book.description?.let { description ->
            Column(
                Modifier.fillMaxWidth()
                    .padding(horizontal = 36.dp)

            ) {
                Text(
                    text = stringResource(Res.string.searched_book_detail_description),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W500,
                    color = Color(0xFF202020),
                )
                Spacer(Modifier.height(16.dp))
                var isOverflowed by remember { mutableStateOf(false) }
                var expand by remember { mutableStateOf(false) }
                val richTextState = rememberRichTextState()
                LaunchedEffect(description) {
                    richTextState.setHtml(description)
                }
                RichText(
                    modifier = Modifier
                        .then(
                            if (expand) Modifier.wrapContentSize()
                            else Modifier.height(300.dp)
                        )
                        .animateContentSize(),
                    state = richTextState,
                    fontSize = 16.sp,
                    color = Color(0xFF707070),
                    overflow = TextOverflow.Ellipsis,
                    onTextLayout = {
                        isOverflowed = it.hasVisualOverflow
                    }
                )
                AnimatedVisibility(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    visible = isOverflowed && expand.not(),
                    enter = fadeIn() + slideInVertically { -it / 2 },
                    exit = fadeOut() + slideOutVertically { it / 2 }
                ) {
                    Row(Modifier.clickable { expand = true }) {
                        Text(
                            text = stringResource(Res.string.searched_book_detail_expend_description),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                            color = Color(0xFFC4C4C4),
                        )
                        Image(
                            painter = painterResource(Res.drawable.icon_nav_arrow_down),
                            contentDescription = stringResource(Res.string.searched_book_detail_expend_description),
                            colorFilter = ColorFilter.tint(Color(0xFFC4C4C4))
                        )
                    }
                }
            }
        }
    }
}