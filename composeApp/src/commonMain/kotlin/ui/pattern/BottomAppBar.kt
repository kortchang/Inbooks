package ui.pattern

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import app.Screen
import co.touchlab.kermit.Logger
import org.jetbrains.compose.resources.painterResource
import ui.foundation.Shadow
import ui.foundation.shadow
import ui.token.reference.Reference
import ui.token.system.AppTheme
import kotlin.reflect.KClass

@Composable
fun BottomAppBar(
    modifier: Modifier = Modifier,
    currentScreenIs: (KClass<out Screen.MajorScreen>) -> Boolean,
    currentScreenIsMajor: Boolean,
    majorScreens: List<Screen.MajorScreen>,
    navigateTo: (Screen.MajorScreen) -> Unit,
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = currentScreenIsMajor,
        enter = fadeIn() + slideInVertically { it / 2 },
        exit = fadeOut() + slideOutVertically { it / 2 }
    ) {
        Row(
            Modifier.shadow(
                Shadow.Drop(
                    color = Reference.Colors.Black.copy(alpha = 0.08f),
                    blur = 20.dp,
                    offsetY = 4.dp
                ),
                shape = CircleShape,
            )
                .background(AppTheme.Colors.background)
        ) {
            majorScreens.forEach {
                BottomAppBarItem(
                    icon = painterResource(it.iconRes),
                    selected = currentScreenIs(it::class),
                    onClick = { navigateTo(it) }
                )
            }
        }
    }
}


@Composable
private fun BottomAppBarItem(
    icon: Painter,
    selected: Boolean,
    onClick: () -> Unit,
) {
    CompositionLocalProvider(
        LocalContentColor provides if (selected) AppTheme.Colors.onBackground else AppTheme.Colors.onBackgroundVariant
    ) {
        Box(
            Modifier
                .clickable(onClick = onClick)
                .padding(16.dp)
                .size(24.dp)
        ) {
            Icon(
                painter = icon,
                contentDescription = null
            )
        }
    }
}

fun Modifier.bottomAppBarPadding(): Modifier = composed {
    val windowInsets = LocalBottomAppBarWindowInsets.current
    LaunchedEffect(windowInsets) {
        Logger.d("[Kort Debug]") { "bottomAppBarWindowInsets: ${windowInsets}" }
    }
    windowInsetsPadding(LocalBottomAppBarWindowInsets.current)
}

val WindowInsets.Companion.bottomAppBar: WindowInsets
    @Composable
    @ReadOnlyComposable
    get() = LocalBottomAppBarWindowInsets.current

val LocalBottomAppBarWindowInsets = staticCompositionLocalOf { WindowInsets(0) }