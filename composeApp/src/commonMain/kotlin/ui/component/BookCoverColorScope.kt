package ui.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.colorspace.ColorSpace
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.graphics.luminance
import com.kmpalette.DominantColorState
import com.kmpalette.color
import com.kmpalette.loader.rememberPainterLoader
import com.kmpalette.palette.graphics.Target
import com.kmpalette.rememberDominantColorState
import io.kamel.core.getOrNull
import io.kamel.image.asyncPainterResource

@Composable
fun rememberBookCoverColor(
    coverUrl: String,
): DominantColorState<ImageBitmap> {
    val loader = rememberPainterLoader()
    val painterResult = asyncPainterResource(coverUrl)
    val surfaceColor = MaterialTheme.colorScheme.surface
    val dominantColorState = rememberDominantColorState(
        isSwatchValid = { swatch ->
            Color(0xFFFFFFFF).luminance()
            val colorLightness = swatch.color
                .convert(ColorSpaces.Oklab)
                .component1()
            val surfaceLightness = surfaceColor
                .convert(ColorSpaces.Oklab)
                .component1()

            (surfaceLightness - colorLightness) > 0.5
        }
    )
    LaunchedEffect(painterResult) {
        painterResult.getOrNull()?.let { painter ->
            dominantColorState.updateFrom(loader.load(painter))
        }
    }

    return dominantColorState
}