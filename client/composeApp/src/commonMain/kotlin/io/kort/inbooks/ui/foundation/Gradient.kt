package io.kort.inbooks.ui.foundation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun Brush.Companion.sweepGradient(
    vararg colorStops:  Pair<Float, Color>,
    center: Offset = Offset.Unspecified,
    degrees: Float = 0f,
): Brush {
    return ActualRotatableSweepGradient(
        center = center,
        colors = colorStops.map { it.second },
        stops = colorStops.map { it.first },
        degrees = degrees,
    )
}

expect fun ActualRotatableSweepGradient(
    center: Offset,
    colors: List<Color>,
    stops: List<Float>,
    degrees: Float,
): Brush