package io.kort.inbooks.ui.resource

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.PlusSmall: ImageVector
    get() {
        if (_PlusSmall != null) {
            return _PlusSmall!!
        }
        _PlusSmall = ImageVector.Builder(
            name = "PlusSmall",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF6F6F6F)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(6f, 12f)
                horizontalLineTo(12f)
                moveTo(18f, 12f)
                horizontalLineTo(12f)
                moveTo(12f, 12f)
                verticalLineTo(6f)
                moveTo(12f, 12f)
                verticalLineTo(18f)
            }
        }.build()

        return _PlusSmall!!
    }

@Suppress("ObjectPropertyName")
private var _PlusSmall: ImageVector? = null