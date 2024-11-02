package io.kort.inbooks.ui.resource

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import kotlin.Suppress

val Icons.Language: ImageVector
    get() {
        if (_Language != null) {
            return _Language!!
        }
        _Language = ImageVector.Builder(
            name = "Language",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF181818)),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(2f, 12f)
                curveTo(2f, 17.523f, 6.477f, 22f, 12f, 22f)
                curveTo(17.523f, 22f, 22f, 17.523f, 22f, 12f)
                curveTo(22f, 6.477f, 17.523f, 2f, 12f, 2f)
                curveTo(6.477f, 2f, 2f, 6.477f, 2f, 12f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF181818)),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(13f, 2.049f)
                curveTo(13f, 2.049f, 16f, 6f, 16f, 12f)
                curveTo(16f, 18f, 13f, 21.951f, 13f, 21.951f)
            }
            path(
                stroke = SolidColor(Color(0xFF181818)),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(11f, 21.951f)
                curveTo(11f, 21.951f, 8f, 18f, 8f, 12f)
                curveTo(8f, 6f, 11f, 2.049f, 11f, 2.049f)
            }
            path(
                stroke = SolidColor(Color(0xFF181818)),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(2.63f, 15.5f)
                horizontalLineTo(21.371f)
            }
            path(
                stroke = SolidColor(Color(0xFF181818)),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(2.63f, 8.5f)
                horizontalLineTo(21.371f)
            }
        }.build()

        return _Language!!
    }

@Suppress("ObjectPropertyName")
private var _Language: ImageVector? = null
