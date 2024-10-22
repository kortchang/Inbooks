package io.kort.inbooks.ui.screen.app

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.findRootCoordinates
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.zIndex
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import io.kort.inbooks.app.di.getViewModel
import io.kort.inbooks.ui.component.Page
import io.kort.inbooks.ui.foundation.LocalNavigationSharedTransitionScope
import io.kort.inbooks.ui.pattern.BottomAppBar
import io.kort.inbooks.ui.pattern.LocalBottomAppBarWindowInsets
import io.kort.inbooks.ui.screen.book.detail.screen.collected.CollectedBookDetailScreen
import io.kort.inbooks.ui.screen.book.detail.screen.searched.SearchedBookDetailScreen
import io.kort.inbooks.ui.screen.book.list.BookListScreen
import io.kort.inbooks.ui.screen.dashboard.DashboardScreen
import io.kort.inbooks.ui.screen.onboarding.OnboardingScreen
import io.kort.inbooks.ui.screen.search.SearchScreen
import io.kort.inbooks.ui.screen.topic.detail.TopicDetailScreen
import io.kort.inbooks.ui.screen.topic.detail.TopicDetailViewModel
import io.kort.inbooks.ui.screen.topic.edit.addOrEditTopicNavigation
import io.kort.inbooks.ui.screen.topic.list.TopicListScreen
import io.kort.inbooks.ui.token.system.System
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppScreen() {
    SharedTransitionLayout {
        val viewModel = getViewModel<AppViewModel>()
        val navController = rememberNavController()
        val currentBackStackEntry by navController.currentBackStackEntryAsState()
        val bottomAppBarVisible: Boolean by remember {
            derivedStateOf {
                currentBackStackEntry?.let { backStack ->
                    Screen.MajorScreens.any { majorScreen ->
                        backStack.destination.hasRoute(majorScreen.first::class)
                    }
                } ?: false
            }
        }

        var bottomAppBarWindowInsets by remember { mutableStateOf(WindowInsets(0)) }
        LaunchedEffect(bottomAppBarVisible) {
            if (bottomAppBarVisible.not()) bottomAppBarWindowInsets = WindowInsets(0)
        }

        CompositionLocalProvider(
            LocalNavigationSharedTransitionScope provides this,
            LocalBottomAppBarWindowInsets provides bottomAppBarWindowInsets,
        ) {
            val isOnboardedOrNull by produceState<Boolean?>(null) {
                value = viewModel.isOnboarded()
            }

            Box(Modifier.fillMaxSize().background(System.colors.surface)) {
                isOnboardedOrNull?.let { isOnboarded ->
                    BottomAppBar(
                        sharedTransitionScope = this@SharedTransitionLayout,
                        bottomAppBarVisible = bottomAppBarVisible,
                        currentBackStackEntry = currentBackStackEntry,
                        navController = navController,
                        onWindowInsetsUpdate = { bottomAppBarWindowInsets = it },
                    )

                    Box(Modifier.fillMaxSize()) {
                        AppNavHost(navController = navController, isOnboarded = isOnboarded)
                    }
                }
            }
        }
    }
}

@Composable
private fun AppNavHost(
    navController: NavHostController,
    isOnboarded: Boolean,
) {
    NavHost(
        navController = navController,
        startDestination = if (isOnboarded) Screen.Dashboard else Screen.Onboarding,
        enterTransition = { fadeIn(tween(durationMillis = 150)) },
        exitTransition = { fadeOut(tween(durationMillis = 150)) },
    ) {
        composable<Screen.Onboarding> {
            Page {
                OnboardingScreen(
                    navigateToDashboard = {
                        navController.popBackStack()
                        navController.navigate(Screen.Dashboard) { launchSingleTop = true }
                    }
                )
            }
        }

        composable<Screen.Dashboard> {
            Page {
                DashboardScreen(
                    navigateToBookDetail = {
                        navController.navigate(Screen.CollectedBookDetail(it.book.id))
                    },
                    navigateToTopic = { navController.navigate(Screen.TopicDetail(it.id)) },
                    navigateToBookList = { navController.navigate(Screen.BookList) },
                    navigateToTopicList = { navController.navigate(Screen.TopicList) },
                )
            }
        }

        composable<Screen.Search> {
            Page {
                SearchScreen(
                    navigateToSearchedDetail = {
                        navController.navigate(Screen.SearchedBookDetail(it.book.externalIds))
                    },
                    navigateToCollectedDetail = {
                        navController.navigate(Screen.CollectedBookDetail(it.book.id))
                    },
                )
            }
        }

        composable<Screen.BookList> {
            Page {
                BookListScreen(
                    navigateToCollectedBookDetail = {
                        navController.navigate(Screen.CollectedBookDetail(it.book.id))
                    },
                )
            }
        }

        composable<Screen.SearchedBookDetail> { backStack ->
            val screen = backStack.toRoute<Screen.SearchedBookDetail>()
            Page {
                SearchedBookDetailScreen(
                    externalIds = screen.externalIds,
                    back = { navController.navigateUp() },
                    navigateToCollectedBookDetail = {
                        navController.navigate(Screen.CollectedBookDetail(it)) {
                            popUpTo(Screen.Dashboard)
                        }
                    },
                )
            }
        }

        composable<Screen.CollectedBookDetail> { backStack ->
            val screen = backStack.toRoute<Screen.CollectedBookDetail>()
            Page {
                CollectedBookDetailScreen(
                    bookId = screen.bookId,
                    back = { navController.navigateUp() },
                    popAndNavigateToSearchedBookDetail = {
                        navController.popBackStack()
                        navController.navigate(Screen.SearchedBookDetail(it.externalIds))
                    },
                )
            }
        }

        composable<Screen.TopicList> {
            Page {
                TopicListScreen(
                    navigateToTopicDetail = { navController.navigate(Screen.TopicDetail(it.id)) },
                    navigateToAddTopic = { navController.navigate(Screen.AddOrEditTopic(null)) },
                )
            }
        }

        addOrEditTopicNavigation(navController)

        composable<Screen.TopicDetail> { backStack ->
            val screen = backStack.toRoute<Screen.TopicDetail>()
            val viewModel = getViewModel<TopicDetailViewModel> { parametersOf(screen.topicId) }
            Page {
                TopicDetailScreen(
                    viewModel = viewModel,
                    back = { navController.navigateUp() },
                    navigateToEditTopic = {
                        navController.navigate(Screen.AddOrEditTopic(screen.topicId))
                    },
                    navigateToCollectedBookDetail = {
                        navController.navigate(Screen.CollectedBookDetail(it.book.id))
                    },
                    navigateToSearchedBookDetail = {
                        navController.navigate(Screen.SearchedBookDetail(it.externalIds))
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun BoxScope.BottomAppBar(
    sharedTransitionScope: SharedTransitionScope,
    bottomAppBarVisible: Boolean,
    currentBackStackEntry: NavBackStackEntry?,
    navController: NavHostController,
    onWindowInsetsUpdate: (WindowInsets) -> Unit,
) {
    with(sharedTransitionScope) {
        BottomAppBar(
            modifier = Modifier
                .zIndex(Float.MAX_VALUE)
                .align(Alignment.BottomCenter)
                .safeDrawingPadding()
                .padding(bottom = System.spacing.pagePadding)
                .onPlaced { layoutCoordinates ->
                    val windowHeight = layoutCoordinates.findRootCoordinates().size.height
                    val topInWindow = layoutCoordinates.boundsInRoot().top
                    onWindowInsetsUpdate(
                        if (bottomAppBarVisible) {
                            WindowInsets(bottom = (windowHeight - topInWindow).toInt())
                        } else {
                            WindowInsets(0)
                        }
                    )
                },
            visible = bottomAppBarVisible,
            currentScreenIs = { currentBackStackEntry?.destination?.hasRoute(it) ?: false },
            majorScreens = Screen.MajorScreens,
            sharedTransitionScope = this,
            navigateTo = {
                navController.navigate(it) {
                    launchSingleTop = true
                }
            },
        )
    }
}