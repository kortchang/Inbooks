package io.kort.inbooks.ui.resource

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.Cell2x2: ImageVector
    get() {
        if (_Cell2x2 != null) {
            return _Cell2x2!!
        }
        _Cell2x2 = ImageVector.Builder(
            name = "Cell2x2",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFFA1A1A1)),
                strokeLineWidth = 1.5f
            ) {
                moveTo(21f, 3.6f)
                verticalLineTo(12f)
                horizontalLineToRelative(-9f)
                verticalLineTo(3f)
                horizontalLineToRelative(8.4f)
                curveTo(20.73f, 3f, 21f, 3.27f, 21f, 3.6f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFFA1A1A1)),
                strokeLineWidth = 1.5f
            ) {
                moveTo(21f, 20.4f)
                verticalLineTo(12f)
                horizontalLineToRelative(-9f)
                verticalLineToRelative(9f)
                horizontalLineToRelative(8.4f)
                curveToRelative(0.33f, 0f, 0.6f, -0.27f, 0.6f, -0.6f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFFA1A1A1)),
                strokeLineWidth = 1.5f
            ) {
                moveTo(3f, 12f)
                verticalLineTo(3.6f)
                curveTo(3f, 3.27f, 3.27f, 3f, 3.6f, 3f)
                horizontalLineTo(12f)
                verticalLineToRelative(9f)
                horizontalLineTo(3f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFFA1A1A1)),
                strokeLineWidth = 1.5f
            ) {
                moveTo(3f, 12f)
                verticalLineToRelative(8.4f)
                curveTo(3f, 20.73f, 3.27f, 21f, 3.6f, 21f)
                horizontalLineTo(12f)
                verticalLineToRelative(-9f)
                horizontalLineTo(3f)
                close()
            }
        }.build()

        return _Cell2x2!!
    }

@Suppress("ObjectPropertyName")
private var _Cell2x2: ImageVector? = null