package io.kort.inbooks.ui.resource

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import kotlin.Suppress

val Icons.ViewRow2: ImageVector
    get() {
        if (_ViewRow2 != null) {
            return _ViewRow2!!
        }
        _ViewRow2 = ImageVector.Builder(
            name = "ViewRow2",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF828282)),
                strokeLineWidth = 1.5f
            ) {
                moveTo(21f, 12f)
                lineTo(21f, 20.4f)
                curveTo(21f, 20.731f, 20.731f, 21f, 20.4f, 21f)
                lineTo(3.6f, 21f)
                curveTo(3.269f, 21f, 3f, 20.731f, 3f, 20.4f)
                lineTo(3f, 12f)
                moveTo(21f, 12f)
                lineTo(21f, 3.6f)
                curveTo(21f, 3.269f, 20.731f, 3f, 20.4f, 3f)
                lineTo(3.6f, 3f)
                curveTo(3.269f, 3f, 3f, 3.269f, 3f, 3.6f)
                lineTo(3f, 12f)
                moveTo(21f, 12f)
                lineTo(3f, 12f)
            }
        }.build()

        return _ViewRow2!!
    }

@Suppress("ObjectPropertyName")
private var _ViewRow2: ImageVector? = null
