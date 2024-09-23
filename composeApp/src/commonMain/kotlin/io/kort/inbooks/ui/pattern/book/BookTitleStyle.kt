package io.kort.inbooks.ui.pattern.book

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.kort.inbooks.domain.model.book.Book

@Composable
fun BookTitle(
    modifier: Modifier = Modifier,
    book: Book,
    additional: @Composable() (() -> Unit)? = null,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = book.title,
            fontSize = 16.sp,
            color = io.kort.inbooks.ui.token.system.System.colors.onSurface,
            fontWeight = FontWeight.W600,
            overflow = TextOverflow.Ellipsis,
        )

        book.authors?.let { authors ->
            Text(
                text = authors.joinToString(" "),
                fontSize = 14.sp,
                color = io.kort.inbooks.ui.token.system.System.colors.onSurfaceVariant,
            )
        }

        additional?.invoke()
    }
}

@Composable
private fun ReadingReasonText(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = 14.sp,
        color = io.kort.inbooks.ui.token.system.System.colors.onSurface,
    )
}