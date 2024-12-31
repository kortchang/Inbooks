package io.kort.inbooks.ui.screen.auth.signUp

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import io.kort.inbooks.ui.screen.app.Screen

fun NavGraphBuilder.signUp(navController: NavHostController) {
    navigation<Screen.SignUp>(startDestination = Screen.SignUp.Email) {
        composable<Screen.SignUp.Email> {

        }
    }
}