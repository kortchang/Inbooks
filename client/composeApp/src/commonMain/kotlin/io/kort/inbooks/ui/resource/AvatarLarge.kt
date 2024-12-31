package io.kort.inbooks.ui.resource

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import kotlin.Suppress

val Icons.AvatarLarge: ImageVector
    get() {
        if (_AvatarLarge != null) {
            return _AvatarLarge!!
        }
        _AvatarLarge = ImageVector.Builder(
            name = "AvatarLarge",
            defaultWidth = 40.dp,
            defaultHeight = 40.dp,
            viewportWidth = 40f,
            viewportHeight = 40f
        ).apply {
            path(fill = SolidColor(Color(0xFF828282))) {
                moveTo(19.845f, 20f)
                curveTo(22.606f, 20f, 24.845f, 17.761f, 24.845f, 15f)
                curveTo(24.845f, 12.239f, 22.606f, 10f, 19.845f, 10f)
                curveTo(17.083f, 10f, 14.845f, 12.239f, 14.845f, 15f)
                curveTo(14.845f, 17.761f, 17.083f, 20f, 19.845f, 20f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF828282)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(34.69f, 33.403f)
                curveTo(31.03f, 37.454f, 25.735f, 40f, 19.845f, 40f)
                curveTo(13.955f, 40f, 8.66f, 37.454f, 5f, 33.403f)
                curveTo(5.928f, 31.668f, 11.006f, 23f, 19.845f, 23f)
                curveTo(28.684f, 23f, 33.762f, 31.668f, 34.69f, 33.403f)
                close()
            }
        }.build()

        return _AvatarLarge!!
    }

@Suppress("ObjectPropertyName")
private var _AvatarLarge: ImageVector? = null
