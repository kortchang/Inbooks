package io.kort.inbooks.ui.foundation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf

@OptIn(ExperimentalSharedTransitionApi::class)
val LocalNavigationSharedTransitionScope = staticCompositionLocalOf<SharedTransitionScope?> { null }
val LocalNavigationAnimatedContentScope = compositionLocalOf<AnimatedContentScope?> { null }