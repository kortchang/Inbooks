package io.kort.inbooks.ui.resource

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.PlusCircle: ImageVector
    get() {
        if (_PlusCircle != null) {
            return _PlusCircle!!
        }
        _PlusCircle = ImageVector.Builder(
            name = "PlusCircle",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF6F6F6F)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(8f, 12f)
                horizontalLineTo(12f)
                moveTo(16f, 12f)
                horizontalLineTo(12f)
                moveTo(12f, 12f)
                verticalLineTo(8f)
                moveTo(12f, 12f)
                verticalLineTo(16f)
            }
            path(
                stroke = SolidColor(Color(0xFF6F6F6F)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(12f, 22f)
                curveTo(17.523f, 22f, 22f, 17.523f, 22f, 12f)
                curveTo(22f, 6.477f, 17.523f, 2f, 12f, 2f)
                curveTo(6.477f, 2f, 2f, 6.477f, 2f, 12f)
                curveTo(2f, 17.523f, 6.477f, 22f, 12f, 22f)
                close()
            }
        }.build()

        return _PlusCircle!!
    }

@Suppress("ObjectPropertyName")
private var _PlusCircle: ImageVector? = null