package io.kort.inbooks.ui.component

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.kort.inbooks.ui.foundation.ClearFocusBox
import io.kort.inbooks.ui.foundation.LocalNavigationAnimatedContentScope
import io.kort.inbooks.ui.pattern.bottomAppBar
import io.kort.inbooks.ui.token.system.System

@Composable
fun AnimatedContentScope.Page(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable PageScope.() -> Unit,
) {
    val pagePadding =
        WindowInsets(
            System.spacing.pagePadding,
            System.spacing.pagePadding,
            System.spacing.pagePadding,
            System.spacing.pagePadding,
        )

    val windowInsets =
        WindowInsets.systemBars
            .union(WindowInsets.bottomAppBar)
            .add(pagePadding)

    CompositionLocalProvider(LocalNavigationAnimatedContentScope provides this) {
        Box(modifier, contentAlignment) {
            val pageScope = remember(windowInsets, this@Page) {
                PageScope(this@Page, windowInsets)
            }

            ClearFocusBox {
                content(pageScope)
            }
        }
    }
}

data class PageScope(
    val animatedContentScope: AnimatedContentScope,
    val windowInsets: WindowInsets
) {
    val bottomWindowInsets get() = windowInsets.only(WindowInsetsSides.Bottom)
    val excludeBottomWindowInsets get() = windowInsets.exclude(bottomWindowInsets)
}