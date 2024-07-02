package ui.foundation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.max
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation

/**
 * 為了讓 Android 也可以享受到 Shadow 的快感，所以做了一組 Shadow Modifier 🎉。
 * 主要分成 Drop and Inner shadow。 Figma 的所有參數都支援 (color, blur, offset, spread)
 */
sealed class Shadow {
    abstract val color: Color
    abstract val blur: Dp
    abstract val offsetX: Dp
    abstract val offsetY: Dp
    abstract val spread: Dp

    data class Drop(
        override val color: Color,
        override val blur: Dp,
        override val offsetX: Dp = 0.dp,
        override val offsetY: Dp = 0.dp,
        override val spread: Dp = 0.dp,
    ) : Shadow()

    data class Inner(
        override val color: Color,
        override val blur: Dp,
        override val offsetX: Dp = 0.dp,
        override val offsetY: Dp = 0.dp,
        override val spread: Dp = 0.dp,
    ) : Shadow()

    /**
     * Compose 原生 Text Shadow 目前只支援 drop shadow.
     */
    @Composable
    fun toTextShadow() =
        Shadow(color,
            with(LocalDensity.current) { Offset(offsetX.toPx(), offsetY.toPx()) },
            with(LocalDensity.current) { blur.toPx() }
        )
}

fun Modifier.shadow(shadow: Shadow, shape: Shape = RectangleShape, clip: Boolean = true) =
    composed {
        when (shadow) {
            is Shadow.Drop -> DropShadowModifier(shadow, shape)
            is Shadow.Inner -> InnerShadowModifier(shadow, shape)
        }.run { if (clip) clip(shape = shape) else this }
    }

expect class DropShadowModifier(shadow: Shadow.Drop, shape: Shape) : DrawModifier
expect class InnerShadowModifier(shadow: Shadow.Inner, shape: Shape) : DrawModifier

expect fun Path.scale(scaleX: Float, scaleY: Float): Path

fun Path.toCenterStrokePath(strokeWidth: Float) = toStrokePath(strokeWidth / 2, strokeWidth / 2)
fun Path.toInnerStrokePath(strokeWidth: Float) = toStrokePath(innerStrokeWidth = strokeWidth)
fun Path.toOuterStrokePath(strokeWidth: Float) = toStrokePath(outerStrokeWidth = strokeWidth)

/**
 * 將目前的 Path 轉換成他的 StrokePath
 *
 * @param outerStrokeWidth 在 Path 之外的 Stroke 大小
 * @param innerStrokeWidth 在 Path 之內的 Stroke 大小
 *
 * @see Path.toCenterStrokePath
 * @see Path.toOuterStrokePath
 * @see Path.toInnerStrokePath
 */
fun Path.toStrokePath(outerStrokeWidth: Float = 0f, innerStrokeWidth: Float = 0f): Path {
    val originPath = this
    val bounds = originPath.getBounds()
    val outerScaleX = (outerStrokeWidth + bounds.width) / bounds.width
    val outerScaleY = (outerStrokeWidth + bounds.height) / bounds.height
    val outerStrokePath = Path().apply { addPath(originPath) }.scale(outerScaleX, outerScaleY)

    val innerScaleX = (bounds.width - innerStrokeWidth) / bounds.width
    val innerScaleY = (bounds.height - innerStrokeWidth) / bounds.height
    val innerScalePath = Path().apply { addPath(originPath) }.scale(innerScaleX, innerScaleY)

    return if (outerStrokeWidth == 0f && innerStrokeWidth == 0f) this
    else Path().apply { op(outerStrokePath, innerScalePath, PathOperation.Xor) }
}