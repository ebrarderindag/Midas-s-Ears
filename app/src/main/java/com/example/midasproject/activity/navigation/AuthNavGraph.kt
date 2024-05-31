package com.example.midasproject.activity.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.midasproject.activity.login.LoginActivity

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    startDestination : String
) {

    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = AuthScreen.Login.route//startDestination
    ){
        composable(route = AuthScreen.Login.route){
            LoginActivity(navController)
        }
    }
}

sealed class AuthScreen(val route: String){
    object Login: AuthScreen(route = "login")
}

