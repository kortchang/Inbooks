package io.kort.inbooks.ui.screen.topic.edit

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import io.kort.inbooks.ui.screen.app.Screen
import io.kort.inbooks.app.di.getViewModel
import io.kort.inbooks.ui.component.Page
import io.kort.inbooks.ui.screen.topic.edit.book.SelectBookScreen
import io.kort.inbooks.ui.screen.topic.edit.book.SelectBookViewModel
import io.kort.inbooks.ui.screen.topic.edit.main.AddOrEditTopicScreen
import io.kort.inbooks.ui.screen.topic.edit.main.AddOrEditTopicUiIntent
import io.kort.inbooks.ui.screen.topic.edit.main.AddOrEditTopicUiState
import io.kort.inbooks.ui.screen.topic.edit.main.AddOrEditTopicViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.addOrEditTopic(navController: NavHostController) {
    navigation<Screen.AddOrEditTopic>(startDestination = Screen.AddOrEditTopic.Main) {
        composable<Screen.AddOrEditTopic.Main>(
            enterTransition = { fadeIn() + slideInVertically { 20 } },
            exitTransition = { fadeOut() },
            popEnterTransition = { fadeIn() },
            popExitTransition = { fadeOut() + slideOutVertically { 20 } },
        ) { backStack ->
            val parentBackStack = remember(backStack) { navController.getBackStackEntry<Screen.AddOrEditTopic>() }
            val viewModel = getViewModel<AddOrEditTopicViewModel>(
                viewModelStoreOwner = parentBackStack
            ) { parametersOf(parentBackStack.toRoute<Screen.AddOrEditTopic>().topicIdForEdit) }

            Page {
                AddOrEditTopicScreen(
                    viewModel = viewModel,
                    back = { navController.navigateUp() },
                    navigateToSelectBook = { navController.navigate(Screen.AddOrEditTopic.SelectBook) }
                )
            }
        }

        composable<Screen.AddOrEditTopic.SelectBook>(
            enterTransition = { fadeIn() + slideInVertically { 20 } },
            exitTransition = { fadeOut() + slideOutVertically { 20 } },
        ) {
            val parentBackStack = remember { navController.getBackStackEntry<Screen.AddOrEditTopic>() }
            val sharedViewModel = getViewModel<AddOrEditTopicViewModel>(parentBackStack) {
                parametersOf(parentBackStack.toRoute<Screen.AddOrEditTopic>().topicIdForEdit)
            }
            val books = (sharedViewModel.uiState.value as? AddOrEditTopicUiState.InitializedUiState)
                ?.topic
                ?.books.orEmpty()

            val viewModel = getViewModel<SelectBookViewModel> { parametersOf(books) }

            Page {
                SelectBookScreen(
                    viewModel = viewModel,
                    back = { navController.navigateUp() },
                    confirm = {
                        sharedViewModel.uiState.value.intentTo(AddOrEditTopicUiIntent.UpdateBooks(it))
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}