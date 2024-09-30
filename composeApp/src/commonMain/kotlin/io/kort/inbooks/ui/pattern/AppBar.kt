package io.kort.inbooks.ui.pattern

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.kort.inbooks.ui.resource.Res
import io.kort.inbooks.ui.resource.app_name
import io.kort.inbooks.ui.component.TopAppBar
import io.kort.inbooks.ui.resource.app_icon
import io.kort.inbooks.ui.token.system.System
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally
) {
    TopAppBar(
        modifier = modifier,
        start = {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp, alignment = horizontalAlignment),
            ) {
                Image(
                    painter = painterResource(Res.drawable.app_icon),
                    contentDescription = null,
                )
                Text(
                    text = stringResource(Res.string.app_name),
                    fontWeight = FontWeight.W600,
                    fontSize = 16.sp,
                    color = System.colors.onSurface,
                )
            }
        }
    )
}