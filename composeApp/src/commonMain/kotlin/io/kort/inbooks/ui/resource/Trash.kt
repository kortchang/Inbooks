package io.kort.inbooks.ui.resource

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import kotlin.Suppress

val Icons.Trash: ImageVector
    get() {
        if (_Trash != null) {
            return _Trash!!
        }
        _Trash = ImageVector.Builder(
            name = "Trash",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFFD93A3A)),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(20f, 9f)
                lineTo(18.005f, 20.346f)
                curveTo(17.837f, 21.303f, 17.006f, 22f, 16.035f, 22f)
                horizontalLineTo(7.965f)
                curveTo(6.994f, 22f, 6.163f, 21.303f, 5.995f, 20.346f)
                lineTo(4f, 9f)
            }
            path(
                stroke = SolidColor(Color(0xFFD93A3A)),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(21f, 6f)
                horizontalLineTo(15.375f)
                moveTo(3f, 6f)
                horizontalLineTo(8.625f)
                moveTo(8.625f, 6f)
                verticalLineTo(4f)
                curveTo(8.625f, 2.895f, 9.52f, 2f, 10.625f, 2f)
                horizontalLineTo(13.375f)
                curveTo(14.48f, 2f, 15.375f, 2.895f, 15.375f, 4f)
                verticalLineTo(6f)
                moveTo(8.625f, 6f)
                horizontalLineTo(15.375f)
            }
        }.build()

        return _Trash!!
    }

@Suppress("ObjectPropertyName")
private var _Trash: ImageVector? = null
