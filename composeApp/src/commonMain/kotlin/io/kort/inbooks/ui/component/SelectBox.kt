package io.kort.inbooks.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.kort.inbooks.ui.resource.CheckSmall
import io.kort.inbooks.ui.resource.Icons
import io.kort.inbooks.ui.token.reference.Reference
import io.kort.inbooks.ui.token.system.System

@Composable
fun SelectBox(selected: Boolean, onSelectedChange: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    val background = if (selected) System.colors.primary else Reference.Colors.Transparent
    val outline = if (selected) Reference.Colors.Transparent else System.colors.primary
    Box(
        modifier = modifier
            .size(24.dp)
            .clip(CircleShape)
            .border(1.dp, outline, CircleShape)
            .background(background)
            .clickable { onSelectedChange(!selected) },
        contentAlignment = Alignment.Center,
    ) {
        if (selected) {
            Icon(
                imageVector = Icons.CheckSmall,
                contentDescription = null,
                tint = System.colors.onPrimary
            )
        }
    }
}