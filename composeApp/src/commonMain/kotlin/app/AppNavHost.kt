package app

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.findRootCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.zIndex
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import book.composeapp.generated.resources.Res
import book.composeapp.generated.resources.icon_cell2x2
import book.composeapp.generated.resources.icon_home_simple_door
import book.composeapp.generated.resources.icon_search
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource
import ui.feature.bookdetail.screen.collected.CollectedBookDetailScreen
import ui.feature.bookdetail.screen.searched.SearchedBookDetailScreen
import ui.feature.dashboard.DashboardScreen
import ui.feature.search.SearchScreen
import ui.pattern.BottomAppBar
import ui.pattern.LocalBottomAppBarWindowInsets
import ui.token.system.AppTheme

sealed interface Screen {
    @Serializable
    data object Dashboard : Screen, MajorScreen {
        override val iconRes: DrawableResource = Res.drawable.icon_home_simple_door
    }

    @Serializable
    data object Search : Screen, MajorScreen {
        override val iconRes: DrawableResource = Res.drawable.icon_search
    }

    @Serializable
    data object Collection : Screen, MajorScreen {
        override val iconRes: DrawableResource = Res.drawable.icon_cell2x2
    }

    @Serializable
    data class SearchedBookDetail(val bookIdBySource: String) : Screen

    @Serializable
    data class CollectedBookDetail(val bookId: String) : Screen

    @Serializable
    data object Settings: Screen

    companion object {
        val MajorScreens: List<MajorScreen> = listOf(Dashboard, Search, Collection)
    }

    interface MajorScreen {
        val iconRes: DrawableResource
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavHost() {
    SharedTransitionLayout {
        val navController = rememberNavController()
        val currentBackStackEntry by navController.currentBackStackEntryAsState()
        val currentScreenIsMajor: Boolean by remember {
            derivedStateOf {
                Screen.MajorScreens.any {
                    currentBackStackEntry?.destination?.hasRoute(it::class) ?: true
                }
            }
        }

        var bottomAppBarWindowInsets by remember { mutableStateOf(WindowInsets(0)) }
        CompositionLocalProvider(LocalBottomAppBarWindowInsets provides bottomAppBarWindowInsets) {
            Box(Modifier.fillMaxSize().background(AppTheme.Colors.surface)) {
                BottomAppBar(
                    modifier = Modifier
                        .zIndex(Float.MAX_VALUE)
                        .align(Alignment.BottomCenter)
                        .safeDrawingPadding()
                        .padding(bottom = AppTheme.Spacing.pagePadding)
                        .onGloballyPositioned { layoutCoordinates ->
                            val windowHeight = layoutCoordinates.findRootCoordinates().size.height
                            val topInWindow = layoutCoordinates.boundsInRoot().top
                            bottomAppBarWindowInsets = WindowInsets(bottom = (windowHeight - topInWindow).toInt())
                        },
                    currentScreenIs = { currentBackStackEntry?.destination?.hasRoute(it) ?: false },
                    currentScreenIsMajor = currentScreenIsMajor,
                    majorScreens = Screen.MajorScreens,
                    navigateTo = { navController.navigate(it) }
                )

                Box(
                    Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.safeDrawing)
                        .padding(AppTheme.Spacing.pagePadding)
                        .consumeWindowInsets(PaddingValues(AppTheme.Spacing.pagePadding))
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Dashboard,
                        enterTransition = { fadeIn(tween(durationMillis = 500)) },
                        exitTransition = { fadeOut(tween(durationMillis = 300)) },
                    ) {
                        composable<Screen.Dashboard> {
                            DashboardScreen(
                                sharedTransitionScope = this@SharedTransitionLayout,
                                animatedContentScope = this,
                                navigateToSettings = { navController.navigate(Screen.Settings) },
                                navigateToBookDetail = { navController.navigate(Screen.CollectedBookDetail(it.book.id)) },
                            )
                        }

                        composable<Screen.Search> {
                            SearchScreen(
                                sharedTransitionScope = this@SharedTransitionLayout,
                                animatedContentScope = this,
                                back = { navController.navigateUp() },
                                navigateToDetail = {
                                    navController.navigate(Screen.SearchedBookDetail(bookIdBySource = it.book.idBySource))
                                }
                            )
                        }

                        composable<Screen.SearchedBookDetail> { backStack ->
                            val screen = backStack.toRoute<Screen.SearchedBookDetail>()
                            SearchedBookDetailScreen(
                                sharedTransitionScope = this@SharedTransitionLayout,
                                animatedContentScope = this,
                                bookIdBySource = screen.bookIdBySource,
                                back = { navController.navigateUp() },
                                navigateToCollectedBookDetail = {
                                    navController.navigate(
                                        Screen.CollectedBookDetail(it),
                                    ) {
                                        popUpTo(Screen.Dashboard)
                                    }
                                }
                            )
                        }

                        composable<Screen.CollectedBookDetail> { backStack ->
                            val screen = backStack.toRoute<Screen.CollectedBookDetail>()
                            CollectedBookDetailScreen(
                                sharedTransitionScope = this@SharedTransitionLayout,
                                animatedContentScope = this,
                                bookId = screen.bookId,
                                back = { navController.navigateUp() }
                            )
                        }
                    }
                }
            }
        }
    }
}