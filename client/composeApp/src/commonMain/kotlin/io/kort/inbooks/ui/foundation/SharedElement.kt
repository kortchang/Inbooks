package io.kort.inbooks.ui.foundation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Rect

@OptIn(ExperimentalSharedTransitionApi::class)
fun Modifier.sharedElement(
    scenes: List<SharedScene>,
    key: String,
    zIndexInOverlay: Float = 0f,
): Modifier = composed {
    if (scenes.isEmpty()) return@composed this
    val sharedTransitionScope = LocalNavigationSharedTransitionScope.current ?: return@composed this
    val animatedContentScope = LocalNavigationAnimatedContentScope.current ?: return@composed this

    var currentModifier = this@sharedElement

    scenes.forEach { scene ->
        currentModifier = with(sharedTransitionScope) {
            currentModifier.sharedElement(
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
    currentModifier
}

enum class SharedScene {
    DashboardAndBookDetail,
    BookListAndBookDetail,
    SearchAndBookDetail,
    TopicDetailAndBookDetail,
    ;

    companion object {
        val ForBookDetail = listOf(
            DashboardAndBookDetail,
            BookListAndBookDetail,
            SearchAndBookDetail,
            TopicDetailAndBookDetail,
        )
    }
}