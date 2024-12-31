package io.kort.inbooks.ui.pattern.book

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.graphics.luminance
import coil3.compose.rememberAsyncImagePainter
import com.kmpalette.DominantColorState
import com.kmpalette.color
import com.kmpalette.loader.rememberPainterLoader
import com.kmpalette.rememberDominantColorState


@Composable
fun rememberBookCoverColor(
    coverUrl: String,
): DominantColorState<ImageBitmap> {
    val loader = rememberPainterLoader()
    val painterState by rememberAsyncImagePainter(coverUrl).state.collectAsState()
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

    LaunchedEffect(painterState) {
        painterState.painter?.let { painter ->
            dominantColorState.updateFrom(loader.load(painter))
        }
    }

    return dominantColorState
}