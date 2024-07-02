package ui.foundation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceIn
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger

@Composable
fun rememberHeightDelegateScrollConnection(
    height: Dp,
): HeightDelegateScrollConnection {
    val density = LocalDensity.current
    return remember { HeightDelegateScrollConnection(height, density) }
}


class HeightDelegateScrollConnection(
    private val height: Dp,
    private val density: Density
) : NestedScrollConnection {
    val progress: Float by derivedStateOf {
        Logger.e("[Kort Debug]") { "yOffset: $yOffset" }
        val heightOffset = -yOffset
        heightOffset / height
    }
    private var scrollY by mutableStateOf(0f)
    private var yOffset by mutableStateOf(0.dp)

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        return with(density) {
            val delta = available.y
            if (scrollY >= 0) {
                val newOffset = yOffset + delta.toDp()
                val previousOffset = yOffset

                yOffset = newOffset.coerceIn(-height, 0.dp)
                val consumed = yOffset - previousOffset
                if (scrollY in (yOffset.toPx())..0f) {
                    val consumedInPx = consumed.toPx()
                    scrollY += delta - consumedInPx
                    Offset(0f, consumedInPx)
                } else {
                    scrollY += delta
                    super.onPreScroll(available, source)
                }
            } else {
                scrollY += delta
                super.onPreScroll(available, source)
            }
        }
    }
}