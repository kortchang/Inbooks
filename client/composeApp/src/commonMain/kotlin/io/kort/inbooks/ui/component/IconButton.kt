package io.kort.inbooks.ui.component

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun IconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    contentDescription: String? = null,
    onClick: () -> Unit,
    colors: ButtonColors = ButtonDefaults.backgroundButtonColors(),
) {
    Button(
        modifier = modifier,
        start = {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
            )
        },
        onClick = onClick,
        colors = colors,
    )
}