package io.kort.inbooks.ui.token

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.max

object ScaleIndication : IndicationNodeFactory {
    override fun create(interactionSource: InteractionSource): DelegatableNode {
        return ScaleIndicationNode(interactionSource)
    }

    override fun hashCode(): Int = -1
    override fun equals(other: Any?) = other === this
}

private class ScaleIndicationNode(
    private val interactionSource: InteractionSource
) : Modifier.Node(), DrawModifierNode {
    var currentAnchor: Offset? = null
    val animatedScaleDp = Animatable(0f)
    private val animationSpec = spring<Float>(stiffness = Spring.StiffnessMedium)

    private suspend fun animateToPressed(anchor: Offset? = null) {
        currentAnchor = anchor
        animatedScaleDp.animateTo(-16f, animationSpec)
    }

    private suspend fun animateToFocused() {
        currentAnchor = null
        animatedScaleDp.animateTo(-12f, animationSpec)
    }

    private suspend fun animateToRelease(pressPosition: Offset) {
        animateToPressed(pressPosition)
        animateToCancel()
    }

    private suspend fun animateToCancel() {
        animatedScaleDp.animateTo(1f, animationSpec)
    }

    override fun onAttach() {
        coroutineScope.launch {
            interactionSource.interactions.collectLatest { interaction ->
                when (interaction) {
                    is PressInteraction.Press -> animateToPressed(interaction.pressPosition)
                    is PressInteraction.Release -> animateToRelease(interaction.press.pressPosition)
                    is PressInteraction.Cancel -> animateToCancel()
                    is HoverInteraction.Enter -> animateToFocused()
                    is HoverInteraction.Exit -> animateToCancel()
                    is FocusInteraction.Focus -> animateToFocused()
                    is FocusInteraction.Unfocus -> animateToCancel()
                    is DragInteraction.Start -> animateToPressed()
                    is DragInteraction.Stop -> animateToCancel()
                    is DragInteraction.Cancel -> animateToCancel()
                }
            }
        }
    }

    override fun ContentDrawScope.draw() {
        val maxSize = max(size.width, size.height)
        val scale = (maxSize + animatedScaleDp.value) / maxSize

        scale(
            scale = scale,
            pivot = currentAnchor ?: center
        ) {
            this@draw.drawContent()
        }
    }
}