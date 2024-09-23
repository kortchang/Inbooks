package io.kort.inbooks.ui.resource

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.CollageFrame: ImageVector
    get() {
        if (_CollageFrame != null) {
            return _CollageFrame!!
        }
        _CollageFrame = ImageVector.Builder(
            name = "CollageFrame",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF6F6F6F)),
                strokeLineWidth = 2f
            ) {
                moveTo(19.4f, 20f)
                horizontalLineTo(4.6f)
                curveTo(4.269f, 20f, 4f, 19.731f, 4f, 19.4f)
                verticalLineTo(4.6f)
                curveTo(4f, 4.269f, 4.269f, 4f, 4.6f, 4f)
                horizontalLineTo(19.4f)
                curveTo(19.731f, 4f, 20f, 4.269f, 20f, 4.6f)
                verticalLineTo(19.4f)
                curveTo(20f, 19.731f, 19.731f, 20f, 19.4f, 20f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF6F6F6F)),
                strokeLineWidth = 2f
            ) {
                moveTo(11f, 12f)
                verticalLineTo(4f)
            }
            path(
                stroke = SolidColor(Color(0xFF6F6F6F)),
                strokeLineWidth = 2f
            ) {
                moveTo(4f, 12f)
                horizontalLineTo(20f)
            }
        }.build()

        return _CollageFrame!!
    }

@Suppress("ObjectPropertyName")
private var _CollageFrame: ImageVector? = null