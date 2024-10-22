package io.kort.inbooks.ui.resource

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import kotlin.Suppress

val Icons.ShareiOS: ImageVector
    get() {
        if (_ShareiOS != null) {
            return _ShareiOS!!
        }
        _ShareiOS = ImageVector.Builder(
            name = "ShareiOS",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF232323)),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(20f, 13f)
                verticalLineTo(19f)
                curveTo(20f, 20.105f, 19.105f, 21f, 18f, 21f)
                horizontalLineTo(6f)
                curveTo(4.895f, 21f, 4f, 20.105f, 4f, 19f)
                verticalLineTo(13f)
            }
            path(
                stroke = SolidColor(Color(0xFF232323)),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(12f, 15f)
                verticalLineTo(3f)
                moveTo(12f, 3f)
                lineTo(8.5f, 6.5f)
                moveTo(12f, 3f)
                lineTo(15.5f, 6.5f)
            }
        }.build()

        return _ShareiOS!!
    }

@Suppress("ObjectPropertyName")
private var _ShareiOS: ImageVector? = null
