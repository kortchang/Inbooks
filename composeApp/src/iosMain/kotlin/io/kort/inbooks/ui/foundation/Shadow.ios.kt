package io.kort.inbooks.ui.foundation

import androidx.compose.ui.draw.DrawModifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.Density
import org.jetbrains.skia.FilterBlurMode
import org.jetbrains.skia.MaskFilter
import org.jetbrains.skia.PaintMode
import kotlin.math.max

actual class DropShadowModifier actual constructor(
    private val shadow: Shadow.Drop,
    private val shape: Shape,
) : DrawModifier {
    // 因為 Compose Paint 壞壞的不支援 MaskFilter 所以這裡還是使用原本的 Paint。
    private val paint = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        color = shadow.color.toArgb()
        mode = PaintMode.FILL
    }

    override fun ContentDrawScope.draw() {
        val shapeOutline = shape.createOutline(drawContext.size, layoutDirection, Density(density))
        val blurPx = shadow.blur.toPx()
        val offsetXPx = shadow.offsetX.toPx()
        val offsetYPx = shadow.offsetY.toPx()

        paint.maskFilter = MaskFilter.makeBlur(
            mode = FilterBlurMode.NORMAL,
            sigma = blurPx,
        )

        val spreadPx = shadow.spread.toPx()
        val size = drawContext.size
        val scaleX = (spreadPx * 2 + size.width) / size.width
        val scaleY = (spreadPx * 2 + size.height) / size.height
        val pivot = Offset(size.width / 2, size.height / 2)

        // 將 Shape 轉為 Path
        val path = Path().apply { addOutline(shapeOutline) }
        drawIntoCanvas { canvas ->
            withTransform(transformBlock = {
                scale(scaleX, scaleY, pivot)
                translate(offsetXPx, offsetYPx)
            }) {
                canvas.drawPath(path, paint.asComposePaint())
            }
        }
        drawContent()
    }
}

actual class InnerShadowModifier actual constructor(
    private val shadow: Shadow.Inner,
    private val shape: Shape,
) : DrawModifier {
    private val paint = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        color = shadow.color.toArgb()
        mode = PaintMode.FILL
    }

    override fun ContentDrawScope.draw() {
        val shapeOutline = shape.createOutline(drawContext.size, layoutDirection, Density(density))
        val path = Path().apply { addOutline(shapeOutline) }

        val blurPx = shadow.blur.toPx()
        val offsetXPx = shadow.offsetX.toPx()
        val offsetYPx = shadow.offsetY.toPx()
        val strokeWidthPx = blurPx + max(offsetXPx, offsetYPx)
        val innerStrokeWidthPx = shadow.spread.toPx()

        paint.apply {
            paint.maskFilter = MaskFilter.makeBlur(
                mode = FilterBlurMode.NORMAL,
                sigma = blurPx,
            )
            strokeWidth = strokeWidthPx
        }

        drawContent()
        drawIntoCanvas { canvas ->
            withTransform(transformBlock = {
                val clipPath = Path().apply { addOutline(shapeOutline) }
                clipPath(clipPath)
                translate(offsetXPx, offsetYPx)
            }) {
                /**
                 * Inner Shadow 的做法是在 Shape 的外圍加入 Border 後模糊，
                 * 但我們 Android Paint 的 Stroke 很神秘的是，他的 Stroke 在內圈的部分，圓角會跟外圈的不一樣，
                 * 內圈的圓角會越來越小，在 Stroke Width 超過一定數字的時候，就會變成沒有圓角了。
                 * 猜測是因為原生不支援 Outer Border 所以他乾脆不處理。 (因為 Center Border 的話，裡面的 Stroke 會被上面的 Shape 蓋住)
                 *
                 * 所以這裡的做法是把兩個 Path 求交集的地方產生 Stroke，然後再模糊，就可以產生漂亮 Der Inner Shadow 了。
                 */
                path.toStrokePath(strokeWidthPx, innerStrokeWidthPx).run {
                    canvas.drawPath(this, paint.asComposePaint())
                }
            }
        }
    }
}

actual fun Path.scale(scaleX: Float, scaleY: Float): Path {
    val scaleMatrix = Matrix()
    scaleMatrix.scale(scaleX, scaleY, 1f)
    return asSkiaPath().apply { transform(scaleMatrix) }.asComposePath()
}