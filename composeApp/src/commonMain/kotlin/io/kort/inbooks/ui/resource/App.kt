package io.kort.inbooks.ui.resource

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import kotlin.Suppress

val Icons.App: ImageVector
    get() {
        if (_App != null) {
            return _App!!
        }
        _App = ImageVector.Builder(
            name = "App",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF181818)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(3.689f, 14.01f)
                curveTo(3.904f, 13.562f, 4.441f, 13.373f, 4.89f, 13.589f)
                lineTo(12f, 17.002f)
                lineTo(19.111f, 13.589f)
                curveTo(19.559f, 13.373f, 20.096f, 13.562f, 20.311f, 14.01f)
                curveTo(20.527f, 14.459f, 20.338f, 14.996f, 19.889f, 15.211f)
                lineTo(12.389f, 18.811f)
                curveTo(12.143f, 18.93f, 11.857f, 18.93f, 11.611f, 18.811f)
                lineTo(4.111f, 15.211f)
                curveTo(3.662f, 14.996f, 3.474f, 14.459f, 3.689f, 14.01f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF717171)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(5.795f, 10.398f)
                curveTo(6.017f, 9.953f, 6.558f, 9.773f, 7.003f, 9.995f)
                lineTo(12f, 12.494f)
                lineTo(16.998f, 9.995f)
                curveTo(17.442f, 9.773f, 17.983f, 9.953f, 18.205f, 10.398f)
                curveTo(18.427f, 10.842f, 18.247f, 11.383f, 17.803f, 11.605f)
                lineTo(12.403f, 14.305f)
                curveTo(12.149f, 14.432f, 11.851f, 14.432f, 11.598f, 14.305f)
                lineTo(6.198f, 11.605f)
                curveTo(5.753f, 11.383f, 5.573f, 10.842f, 5.795f, 10.398f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFA1A1A1)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(8.195f, 7.097f)
                curveTo(8.417f, 6.653f, 8.958f, 6.473f, 9.403f, 6.695f)
                lineTo(12f, 7.994f)
                lineTo(14.598f, 6.695f)
                curveTo(15.042f, 6.473f, 15.583f, 6.653f, 15.805f, 7.097f)
                curveTo(16.027f, 7.542f, 15.847f, 8.083f, 15.403f, 8.305f)
                lineTo(12.403f, 9.805f)
                curveTo(12.149f, 9.932f, 11.851f, 9.932f, 11.598f, 9.805f)
                lineTo(8.598f, 8.305f)
                curveTo(8.153f, 8.083f, 7.973f, 7.542f, 8.195f, 7.097f)
                close()
            }
        }.build()

        return _App!!
    }

@Suppress("ObjectPropertyName")
private var _App: ImageVector? = null
