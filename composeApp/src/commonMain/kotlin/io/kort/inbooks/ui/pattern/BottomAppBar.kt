package io.kort.inbooks.ui.pattern

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import io.kort.inbooks.ui.screen.app.Screen
import io.kort.inbooks.ui.foundation.Shadow
import io.kort.inbooks.ui.foundation.shadow
import io.kort.inbooks.ui.token.reference.Reference
import io.kort.inbooks.ui.token.system.System
import kotlin.reflect.KClass

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun BottomAppBar(
    visible: Boolean,
    currentScreenIs: (KClass<out Screen>) -> Boolean,
    majorScreens: List<Pair<Screen, ImageVector>>,
    sharedTransitionScope: SharedTransitionScope,
    navigateTo: (Screen) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = visible,
        enter = slideInVertically { it / 2 },
        exit = slideOutVertically { it / 2 }
    ) {
        val animatedAlpha by transition.animateFloat { targetState ->
            if (targetState == EnterExitState.Visible) 1f else 0f
        }

        val shadow = System.shadow.medium
        val shadowColor = shadow.color
        val animatedShadowColor by transition.animateColor { targetValue ->
            if (targetValue == EnterExitState.Visible) shadowColor else shadowColor.copy(alpha = 0f)
        }

        with(sharedTransitionScope) {
            Row(
                Modifier
                    .renderInSharedTransitionScopeOverlay(zIndexInOverlay = 1f)
                    .shadow(
                        shadow = when (shadow) {
                            is Shadow.Drop -> shadow.copy(color = animatedShadowColor)
                            is Shadow.Inner -> shadow.copy(color = animatedShadowColor)
                        },
                        shape = CircleShape,
                    )
                    .graphicsLayer { alpha = animatedAlpha }
                    .background(System.colors.background, CircleShape)
            ) {
                majorScreens.forEach { (screen, icon) ->
                    val selected = currentScreenIs(screen::class)
                    BottomAppBarItem(
                        icon = icon,
                        selected = selected,
                        onClick = { if (selected.not() && transition.isRunning.not()) navigateTo(screen) }
                    )
                }
            }
        }
    }
}


@Composable
private fun BottomAppBarItem(
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
) {
    CompositionLocalProvider(
        LocalContentColor provides if (selected) System.colors.onBackground else System.colors.onBackgroundVariant
    ) {
        Box(
            Modifier
                .clickable(onClick = onClick)
                .padding(16.dp)
                .size(24.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null
            )
        }
    }
}

val WindowInsets.Companion.bottomAppBar: WindowInsets
    @Composable
    @ReadOnlyComposable
    get() = LocalBottomAppBarWindowInsets.current

val LocalBottomAppBarWindowInsets = compositionLocalOf { WindowInsets(0) }