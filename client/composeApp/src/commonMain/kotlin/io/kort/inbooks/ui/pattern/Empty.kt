package io.kort.inbooks.ui.pattern

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.kort.inbooks.ui.token.system.System

@Composable
fun Empty(
    illustration: Painter,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier.align(BiasAlignment(horizontalBias = 0f, verticalBias = -0.5f)),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = illustration,
                contentDescription = null
            )
            Spacer(Modifier.height(64.dp))
            Text(
                text = title,
                color = System.colors.onSurface,
                fontSize = 20.sp,
                fontWeight = FontWeight.W600,
                textAlign = TextAlign.Center,
                minLines = 2,
            )
            Text(
                text = description,
                color = System.colors.onSurfaceVariant,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                minLines = 3,
            )
        }
    }
}