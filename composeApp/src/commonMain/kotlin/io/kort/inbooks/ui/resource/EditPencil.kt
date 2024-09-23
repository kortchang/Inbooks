package io.kort.inbooks.ui.resource

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import kotlin.Suppress

val Icons.EditPencil: ImageVector
    get() {
        if (_EditPencil != null) {
            return _EditPencil!!
        }
        _EditPencil = ImageVector.Builder(
            name = "EditPencil",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF000000)),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(14.363f, 5.652f)
                lineTo(15.843f, 4.172f)
                curveTo(16.624f, 3.391f, 17.89f, 3.391f, 18.672f, 4.172f)
                lineTo(20.086f, 5.586f)
                curveTo(20.867f, 6.367f, 20.867f, 7.633f, 20.086f, 8.414f)
                lineTo(18.606f, 9.894f)
                moveTo(14.363f, 5.652f)
                lineTo(4.747f, 15.267f)
                curveTo(4.415f, 15.599f, 4.211f, 16.038f, 4.169f, 16.505f)
                lineTo(3.927f, 19.246f)
                curveTo(3.873f, 19.866f, 4.391f, 20.385f, 5.011f, 20.33f)
                lineTo(7.752f, 20.088f)
                curveTo(8.22f, 20.047f, 8.658f, 19.842f, 8.99f, 19.51f)
                lineTo(18.606f, 9.894f)
                moveTo(14.363f, 5.652f)
                lineTo(18.606f, 9.894f)
            }
        }.build()

        return _EditPencil!!
    }

@Suppress("ObjectPropertyName")
private var _EditPencil: ImageVector? = null
