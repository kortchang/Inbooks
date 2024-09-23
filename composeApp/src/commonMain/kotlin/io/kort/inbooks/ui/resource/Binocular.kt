package io.kort.inbooks.ui.resource

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.Binocular: ImageVector
    get() {
        if (_Binocular != null) {
            return _Binocular!!
        }
        _Binocular = ImageVector.Builder(
            name = "Binocular",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF7C7C7C)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(21.5f, 14f)
                lineTo(20f, 9f)
                reflectiveCurveToRelative(-0.5f, -2f, -2.5f, -2f)
                curveToRelative(0f, 0f, 0f, -2f, -2f, -2f)
                reflectiveCurveToRelative(-2f, 2f, -2f, 2f)
                horizontalLineToRelative(-3f)
                reflectiveCurveToRelative(0f, -2f, -2f, -2f)
                reflectiveCurveToRelative(-2f, 2f, -2f, 2f)
                curveTo(4.5f, 7f, 4f, 9f, 4f, 9f)
                lineToRelative(-1.5f, 5f)
            }
            path(
                stroke = SolidColor(Color(0xFF7C7C7C)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(6f, 20f)
                curveToRelative(2.2f, 0f, 4f, -1.8f, 4f, -4f)
                reflectiveCurveToRelative(-1.8f, -4f, -4f, -4f)
                reflectiveCurveToRelative(-4f, 1.8f, -4f, 4f)
                reflectiveCurveToRelative(1.8f, 4f, 4f, 4f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF7C7C7C)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(18f, 20f)
                curveToRelative(2.2f, 0f, 4f, -1.8f, 4f, -4f)
                reflectiveCurveToRelative(-1.8f, -4f, -4f, -4f)
                reflectiveCurveToRelative(-4f, 1.8f, -4f, 4f)
                reflectiveCurveToRelative(1.8f, 4f, 4f, 4f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF7C7C7C)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(12f, 16f)
                curveToRelative(1.1f, 0f, 2f, -0.9f, 2f, -2f)
                reflectiveCurveToRelative(-0.9f, -2f, -2f, -2f)
                reflectiveCurveToRelative(-2f, 0.9f, -2f, 2f)
                reflectiveCurveToRelative(0.9f, 2f, 2f, 2f)
                close()
            }
        }.build()

        return _Binocular!!
    }

@Suppress("ObjectPropertyName")
private var _Binocular: ImageVector? = null