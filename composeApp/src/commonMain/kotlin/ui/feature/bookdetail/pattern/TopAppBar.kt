package ui.feature.bookdetail.pattern

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import book.composeapp.generated.resources.Res
import book.composeapp.generated.resources.icon_nav_arrow_left
import domain.book.Book
import org.jetbrains.compose.resources.painterResource
import ui.foundation.Shadow
import ui.foundation.shadow

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.detailTopAppBar(
    lazyListState: LazyListState,
    book: Book,
    back: () -> Unit,
) {
   stickyHeader {
       Row(
           modifier = Modifier
               .systemBarsPadding()
               .padding(top = 12.dp)
               .padding(start = 16.dp, end = 36.dp),
           verticalAlignment = Alignment.CenterVertically,
           horizontalArrangement = Arrangement.spacedBy(8.dp),
       ) {
           Image(
               modifier = Modifier
                   .size(48.dp)
                   .then(
                       if (lazyListState.canScrollBackward) {
                           Modifier
                               .shadow(
                                   Shadow.Drop(
                                       Color.Black.copy(alpha = 0.04f),
                                       blur = 20.dp,
                                       offsetY = 5.dp
                                   ),
                                   shape = RoundedCornerShape(16.dp),
                               )
                               .background(Color.White, shape = RoundedCornerShape(16.dp))
                       } else {
                           Modifier
                       }
                   )
                   .clickable(onClick = back)
                   .wrapContentSize(align = Alignment.Center)
                   .size(24.dp),
               painter = painterResource(Res.drawable.icon_nav_arrow_left),
               contentDescription = null,
               colorFilter = ColorFilter.tint(Color(0xFF6F6F6F))
           )
           SmallBookRowIfScrolled(lazyListState = lazyListState, book = book)
       }
   }
}