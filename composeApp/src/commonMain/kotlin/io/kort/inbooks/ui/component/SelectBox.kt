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

@Composable
fun SelectBox(modifier: Modifier = Modifier, selected: Boolean, onSelectedChange: (Boolean) -> Unit) {
    val background = if (selected) io.kort.inbooks.ui.token.system.System.colors.primary else Reference.Colors.Transparent
    val outline = if (selected) Reference.Colors.Transparent else Reference.Colors.Grey.L200
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
                tint = io.kort.inbooks.ui.token.system.System.colors.onPrimary
            )
        }
    }
}