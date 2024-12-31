package io.kort.inbooks.ui.screen.auth.login

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import io.kort.inbooks.ui.component.Page
import io.kort.inbooks.ui.screen.app.Screen
import io.kort.inbooks.ui.screen.auth.login.email.LoginByEmailScreen

fun NavGraphBuilder.login(navController: NavHostController) {
    navigation<Screen.Login>(Screen.Login.Email) {
        composable<Screen.Login.Email> {
            Page {
                LoginByEmailScreen(
                    back = { navController.popBackStack() },
                )
            }
        }

        composable<Screen.Login.VerifyEmail> {

        }

        composable<Screen.Login.Success> {

        }
    }
}