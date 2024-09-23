package io.kort.inbooks.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.kort.inbooks.ui.token.system.System

@Composable
fun InteractionBox(
    modifier: Modifier = Modifier,
    rounded: Boolean = false,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable BoxScope.() -> Unit,
) {
    val shape = if (rounded) CircleShape else RoundedCornerShape(16.dp)
    Box(
        modifier
            .border(1.dp, System.colors.outline, shape = shape)
            .background(System.colors.background, shape = shape)
            .padding(16.dp),
        contentAlignment = contentAlignment,
    ) {
        CompositionLocalProvider(LocalContentColor provides System.colors.onBackground) {
            content()
        }
    }
}

