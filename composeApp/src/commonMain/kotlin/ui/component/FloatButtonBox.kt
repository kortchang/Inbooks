package ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.foundation.Shadow
import ui.foundation.shadow

@Composable
fun FloatButtonBox(
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier
            .shadow(
                Shadow.Drop(
                    Color.Black.copy(alpha = 0.04f),
                    blur = 20.dp,
                    offsetY = 5.dp
                ),
                shape = RoundedCornerShape(16.dp),
            )
            .background(Color.White)
            .padding(padding),
        contentAlignment = contentAlignment,
        content = content,
    )
}