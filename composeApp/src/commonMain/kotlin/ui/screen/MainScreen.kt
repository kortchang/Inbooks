package ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import book.composeapp.generated.resources.Res
import book.composeapp.generated.resources.icon_search
import org.jetbrains.compose.resources.painterResource
import ui.foundation.Shadow
import ui.foundation.shadow
import ui.token.ScaleIndicationNodeFactory

@Composable
fun MainScreen(
    navigateToSearch: () -> Unit
) {
    Surface {
        Column(
            modifier = Modifier.fillMaxSize().safeContentPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.weight(1f))
            FloatButton(Modifier, onClick = { navigateToSearch() })
            Spacer(Modifier.height(36.dp))
        }
    }
}

@Composable
private fun FloatButton(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Box(
        modifier
            .clickable(onClick = onClick,)
            .widthIn(min = 144.dp)
            .shadow(
                Shadow.Drop(
                    Color.Black.copy(alpha = 0.04f),
                    blur = 20.dp,
                    offsetY = 5.dp
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .background(Color.White)

            .padding(horizontal = 24.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.icon_search),
            contentDescription = null,
        )
    }
}