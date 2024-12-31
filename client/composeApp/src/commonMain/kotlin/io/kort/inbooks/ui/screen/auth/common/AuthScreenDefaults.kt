package io.kort.inbooks.ui.screen.auth.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.kort.inbooks.ui.token.system.System

object AuthScreenDefaults {
    @Composable
    fun Title(
        icon: ImageVector,
        title: String,
        modifier: Modifier = Modifier,
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = System.colors.onSurfaceVariant,
            )
            Text(
                title,
                fontSize = 20.sp,
                lineHeight = 20.sp * 1.2f,
                color = System.colors.onSurface,
            )
        }
    }
}