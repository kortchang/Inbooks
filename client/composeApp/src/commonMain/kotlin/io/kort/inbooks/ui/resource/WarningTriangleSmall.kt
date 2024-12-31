package io.kort.inbooks.ui.resource

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import kotlin.Suppress

val Icons.WarningTriangleSmall: ImageVector
    get() {
        if (_WarningTriangleSmall != null) {
            return _WarningTriangleSmall!!
        }
        _WarningTriangleSmall = ImageVector.Builder(
            name = "WarningTriangleSmall",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFFF13D41)),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round
            ) {
                moveTo(12.21f, 14f)
                horizontalLineTo(3.79f)
                curveTo(2.252f, 14f, 1.29f, 12.336f, 2.057f, 11.003f)
                lineTo(6.266f, 3.682f)
                curveTo(7.035f, 2.345f, 8.965f, 2.345f, 9.734f, 3.682f)
                lineTo(13.943f, 11.003f)
                curveTo(14.71f, 12.336f, 13.748f, 14f, 12.21f, 14f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFFF13D41)),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round
            ) {
                moveTo(8f, 6f)
                verticalLineTo(8.667f)
            }
            path(
                stroke = SolidColor(Color(0xFFF13D41)),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(8f, 11.34f)
                lineTo(8.007f, 11.333f)
            }
        }.build()

        return _WarningTriangleSmall!!
    }

@Suppress("ObjectPropertyName")
private var _WarningTriangleSmall: ImageVector? = null
