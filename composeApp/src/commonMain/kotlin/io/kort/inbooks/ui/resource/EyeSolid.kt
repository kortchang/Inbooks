package io.kort.inbooks.ui.resource

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.EyeSolid: ImageVector
    get() {
        if (_EyeSolid != null) {
            return _EyeSolid!!
        }
        _EyeSolid = ImageVector.Builder(
            name = "EyeSolid",
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
                moveTo(3f, 13f)
                curveTo(6.6f, 5f, 17.4f, 5f, 21f, 13f)
            }
            path(
                fill = SolidColor(Color(0xFF6F6F6F)),
                stroke = SolidColor(Color(0xFF6F6F6F)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(12f, 17f)
                curveTo(10.343f, 17f, 9f, 15.657f, 9f, 14f)
                curveTo(9f, 12.343f, 10.343f, 11f, 12f, 11f)
                curveTo(13.657f, 11f, 15f, 12.343f, 15f, 14f)
                curveTo(15f, 15.657f, 13.657f, 17f, 12f, 17f)
                close()
            }
        }.build()

        return _EyeSolid!!
    }

@Suppress("ObjectPropertyName")
private var _EyeSolid: ImageVector? = null