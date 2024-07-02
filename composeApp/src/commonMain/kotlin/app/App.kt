package app

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.screen.searcheddetail.SearchedBookDetailScreen
import ui.screen.MainScreen
import ui.screen.search.SearchScreen
import ui.token.AppTheme

@Composable
@Preview
fun App() {
    AppTheme {
        val navController = rememberNavController()
        NavHost(navController, startDestination = "main") {
            composable("main") {
                MainScreen(
                    navigateToSearch = { navController.navigate("search") }
                )
            }
            composable("search") {
                SearchScreen(
                    back = { navController.navigateUp() },
                    navigateToDetail = {
                        navController.navigate("book-detail/${it.id}")
                    }
                )
            }

            composable("book-detail/{bookId}", arguments = listOf(
                navArgument("bookId") { type = NavType.StringType }
            )) { backStack ->
                val bookId = backStack.arguments?.getString("bookId") ?: return@composable
                SearchedBookDetailScreen(
                    id = bookId,
                    back = { navController.navigateUp() }
                )
            }
        }
    }
}