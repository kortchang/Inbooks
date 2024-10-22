package io.kort.inbooks.ui.pattern.book

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.kort.inbooks.ui.token.system.System

@Composable
fun BookDetail(
    cover: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    title: @Composable (RowScope.() -> Unit)? = null,
    additional: @Composable (ColumnScope.() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    val space = 16.dp
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space),
    ) {
        Row(
            modifier = Modifier.then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
            horizontalArrangement = Arrangement.spacedBy(space)
        ) {
            cover()
            title?.invoke(this)
        }
        additional?.invoke(this)
    }
}

object BookDetailDefaults {
    @Composable
    fun Reason(reason: String?, modifier: Modifier = Modifier) {
        if (reason.isNullOrBlank()) return
        Row(
            modifier = modifier.height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Spacer(
                modifier = Modifier.fillMaxHeight()
                    .padding(vertical = 4.dp)
                    .width(3.dp).clip(CircleShape)
                    .background(System.colors.outline)
            )
            Text(
                text = reason,
                fontSize = 14.sp,
                color = System.colors.onSurface,
            )
        }
    }
}