package io.kort.inbooks.ui.resource

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.HomeSimpleDoor: ImageVector
    get() {
        if (_HomeSimpleDoor != null) {
            return _HomeSimpleDoor!!
        }
        _HomeSimpleDoor = ImageVector.Builder(
            name = "HomeSimpleDoor",
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
                moveTo(9f, 21f)
                horizontalLineTo(7f)
                curveToRelative(-2.2f, 0f, -4f, -1.8f, -4f, -4f)
                verticalLineToRelative(-6.3f)
                curveTo(3f, 9.3f, 3.73f, 8.02f, 4.93f, 7.3f)
                lineToRelative(5f, -3.03f)
                curveToRelative(1.27f, -0.78f, 2.87f, -0.78f, 4.14f, 0f)
                lineToRelative(5f, 3.03f)
                curveTo(20.27f, 8f, 21f, 9.3f, 21f, 10.7f)
                verticalLineTo(17f)
                curveToRelative(0f, 2.2f, -1.8f, 4f, -4f, 4f)
                horizontalLineToRelative(-2f)
                moveToRelative(-6f, 0f)
                verticalLineToRelative(-4f)
                curveToRelative(0f, -1.66f, 1.34f, -3f, 3f, -3f)
                verticalLineToRelative(0f)
                curveToRelative(1.66f, 0f, 3f, 1.34f, 3f, 3f)
                verticalLineToRelative(4f)
                moveToRelative(-6f, 0f)
                horizontalLineToRelative(6f)
            }
        }.build()

        return _HomeSimpleDoor!!
    }

@Suppress("ObjectPropertyName")
private var _HomeSimpleDoor: ImageVector? = null