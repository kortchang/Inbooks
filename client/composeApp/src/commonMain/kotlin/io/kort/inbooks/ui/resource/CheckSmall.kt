package io.kort.inbooks.ui.resource

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import kotlin.Suppress

val Icons.CheckSmall: ImageVector
    get() {
        if (_CheckSmall != null) {
            return _CheckSmall!!
        }
        _CheckSmall = ImageVector.Builder(
            name = "CheckSmall",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF179732)),
                strokeLineWidth = 1.33f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(3.333f, 8.667f)
                lineTo(6f, 11.333f)
                lineTo(12.667f, 4.667f)
            }
        }.build()

        return _CheckSmall!!
    }

@Suppress("ObjectPropertyName")
private var _CheckSmall: ImageVector? = null
