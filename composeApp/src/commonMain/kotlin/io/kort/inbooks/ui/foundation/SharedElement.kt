package io.kort.inbooks.ui.foundation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Rect

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Modifier.sharedElement(
    scenes: List<SharedScene>,
    key: String,
    zIndexInOverlay: Float = 0f,
): Modifier {
    if (scenes.isEmpty()) return this
    val sharedTransitionScope = LocalNavigationSharedTransitionScope.current ?: return this
    val animatedContentScope = LocalNavigationAnimatedContentScope.current ?: return this

    return with(sharedTransitionScope) {
        scenes.fold(this@sharedElement) { modifier, scene ->
            modifier.sharedElement(
                state = sharedTransitionScope.rememberSharedContentState("${scene}_$key"),
                animatedVisibilityScope = animatedContentScope,
                zIndexInOverlay = zIndexInOverlay,
                boundsTransform = { _, _ ->
                    spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow,
                        visibilityThreshold = Rect.VisibilityThreshold
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
fun Modifier.sharedBounds(
    scenes: List<SharedScene>,
    key: String,
    zIndexInOverlay: Float = 0f,
): Modifier = composed {
    if (scenes.isEmpty()) return@composed this
    val sharedTransitionScope = LocalNavigationSharedTransitionScope.current ?: return@composed this
    val animatedContentScope = LocalNavigationAnimatedContentScope.current ?: return@composed this

    with(sharedTransitionScope) {
        scenes.fold(this@sharedBounds) { modifier, scene ->
            modifier.sharedBounds(
                sharedContentState = sharedTransitionScope.rememberSharedContentState("${scene}_$key"),
                animatedVisibilityScope = animatedContentScope,
                zIndexInOverlay = zIndexInOverlay
            )
        }
    }
}

enum class SharedScene {
    DashboardAndBookDetail,
    BookListAndBookDetail,
    SearchAndBookDetail,
    SelectBookAndBookDetail,
    TopicDetailAndBookDetail,
    ;

    companion object {
        val ForBookDetail
            get() = listOf(
                DashboardAndBookDetail,
                BookListAndBookDetail,
                SearchAndBookDetail,
                SelectBookAndBookDetail,
                TopicDetailAndBookDetail,
            )
    }
}