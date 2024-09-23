package io.kort.inbooks.ui.foundation

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalFocusManager
import kotlinx.collections.immutable.persistentHashMapOf
import kotlin.uuid.Uuid

@Composable
fun ClearFocusBox(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val dontClearFocusPositions = remember { DontClearFocusPositions() }
    val focusManager = LocalFocusManager.current
    Box(
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures(
                onPress = {
                    /**
                     * 如果有其他 intercept touch 事件的事件就不會到這裡，所以等於做到想要做的功能
                     */
                    with(dontClearFocusPositions) {
                        if (it.canClearFocus()) focusManager.clearFocus()
                    }
                }
            )
        }
    ) {
        CompositionLocalProvider(LocalDontClearFocusPositions provides dontClearFocusPositions) {
            content()
        }
    }
}

class DontClearFocusPositions {
    private var positions by mutableStateOf(persistentHashMapOf<String, Rect>())
    fun update(id: String, positionInRoot: Rect) {
        positions = positions.put(id, positionInRoot)
    }

    fun Offset.canClearFocus(): Boolean {
        return positions.values.none { position -> position.contains(this) }
    }
}

@Composable
fun Modifier.dontClearFocus(): Modifier {
    val positions = LocalDontClearFocusPositions.current ?: return this
    val id = remember { Uuid.random().toString() }
    return this.onGloballyPositioned {
        positions.update(id, it.boundsInRoot())
    }
}

val LocalDontClearFocusPositions = compositionLocalOf<DontClearFocusPositions?> { null }