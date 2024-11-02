package io.kort.inbooks.ui.resource

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import kotlin.Suppress

val Icons.Undo: ImageVector
    get() {
        if (_Undo != null) {
            return _Undo!!
        }
        _Undo = ImageVector.Builder(
            name = "Undo",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF292929)),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(4.5f, 8f)
                curveTo(8.5f, 8f, 11f, 8f, 15f, 8f)
                curveTo(15f, 8f, 15f, 8f, 15f, 8f)
                curveTo(15f, 8f, 20f, 8f, 20f, 12.706f)
                curveTo(20f, 18f, 15f, 18f, 15f, 18f)
                curveTo(11.571f, 18f, 9.714f, 18f, 6.286f, 18f)
            }
            path(
                stroke = SolidColor(Color(0xFF292929)),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(7.5f, 11.5f)
                curveTo(6.133f, 10.133f, 5.367f, 9.367f, 4f, 8f)
                curveTo(5.367f, 6.633f, 6.133f, 5.867f, 7.5f, 4.5f)
            }
        }.build()

        return _Undo!!
    }

@Suppress("ObjectPropertyName")
private var _Undo: ImageVector? = null
