package io.kort.inbooks.ui.foundation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.geometry.isUnspecified
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.toArgb
import org.jetbrains.skia.FilterTileMode
import org.jetbrains.skia.GradientStyle
import org.jetbrains.skia.Shader

actual fun ActualRotatableSweepGradient(
    center: Offset,
    colors: List<Color>,
    stops: List<Float>,
    degrees: Float,
): Brush {
    return RotatableSweepGradient(
        center = center,
        colors = colors,
        stops = stops,
        degrees = degrees,
    )
}

class RotatableSweepGradient(
    private val center: Offset,
    private val colors: List<Color>,
    private val stops: List<Float>,
    private val degrees: Float,
) : ShaderBrush() {
    override fun createShader(size: Size): Shader {
        val center = if (center.isUnspecified) {
            size.center
        } else {
            Offset(
                if (center.x == Float.POSITIVE_INFINITY) size.width else center.x,
                if (center.y == Float.POSITIVE_INFINITY) size.height else center.y
            )
        }

        return Shader.makeSweepGradient(
            x = center.x,
            y = center.y,
            colors = colors.map { it.toArgb() }.toIntArray(),
            positions = stops.toFloatArray(),
            startAngle = degrees,
            endAngle = 360 + degrees,
            style = GradientStyle.DEFAULT.withTileMode(FilterTileMode.REPEAT)
        )
    }
}