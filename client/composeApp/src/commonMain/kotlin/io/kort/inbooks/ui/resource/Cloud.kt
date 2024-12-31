package io.kort.inbooks.ui.resource

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import kotlin.Suppress

val Icons.Cloud: ImageVector
    get() {
        if (_Cloud != null) {
            return _Cloud!!
        }
        _Cloud = ImageVector.Builder(
            name = "Cloud",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF292929)),
                strokeLineWidth = 2f,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(12f, 4f)
                curveTo(6f, 4f, 6f, 8f, 6f, 10f)
                curveTo(4.333f, 10f, 1f, 11f, 1f, 15f)
                curveTo(1f, 19f, 4.333f, 20f, 6f, 20f)
                horizontalLineTo(18f)
                curveTo(19.667f, 20f, 23f, 19f, 23f, 15f)
                curveTo(23f, 11f, 19.667f, 10f, 18f, 10f)
                curveTo(18f, 8f, 18f, 4f, 12f, 4f)
                close()
            }
        }.build()

        return _Cloud!!
    }

@Suppress("ObjectPropertyName")
private var _Cloud: ImageVector? = null
