package io.kort.inbooks.ui.resource

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import kotlin.Suppress

val Icons.Xmark: ImageVector
    get() {
        if (_Xmark != null) {
            return _Xmark!!
        }
        _Xmark = ImageVector.Builder(
            name = "Xmark",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF2B2B2B)),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(6.757f, 17.243f)
                lineTo(12f, 12f)
                moveTo(17.243f, 6.757f)
                lineTo(12f, 12f)
                moveTo(12f, 12f)
                lineTo(6.757f, 6.757f)
                moveTo(12f, 12f)
                lineTo(17.243f, 17.243f)
            }
        }.build()

        return _Xmark!!
    }

@Suppress("ObjectPropertyName")
private var _Xmark: ImageVector? = null
