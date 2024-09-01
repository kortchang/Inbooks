package ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Button(
    start: @Composable (RowScope.() -> Unit)? = null,
    text: @Composable (RowScope.() -> Unit)? = null,
    end: @Composable (RowScope.() -> Unit)? = null,
) {
    val containerPaddingValues = PaddingValues(
        start = 12.dp,
        top = 12.dp,
        end = if (text != null && end == null) 16.dp else 12.dp,
        bottom = 12.dp
    )
    Row(
        modifier = Modifier.padding(containerPaddingValues),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        start?.invoke(this)
        text?.invoke(this)
        end?.invoke(this)
    }
}