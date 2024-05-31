package com.example.midasproject.activity.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.midasproject.activity.home.ChatboxDetailActivity
import com.example.midasproject.activity.home.HomeActivity
import com.example.midasproject.activity.signUp.SignUpActivity

//import com.example.midasproject.presentation.bottomBar.BottombarNavigation

@Composable
fun RootNavigationGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController ,
        route = Graph.ROOT,
        startDestination = Graph.AUTHENTICATION
    ){
        authNavGraph(navController = navController, startDestination)

        composable(route = "${Graph.HOME}/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            HomeActivity(navController = navController)
        }
        composable(route = Graph.REGISTER){
           SignUpActivity(navController = navController)
        }
        composable(route = "${Graph.CHAT}/{userID}/{chatboxID}/{deger}") { backStackEntry ->
            val userID = backStackEntry.arguments?.getString("userID")
            val chatboxID = backStackEntry.arguments?.getString("chatboxID")
            val deger = backStackEntry.arguments?.getString("deger")
            ChatboxDetailActivity(navController = navController)
        }

    }
}

object Graph{
    const val ROOT = "root_graph"
    const val AUTHENTICATION = "auth_graph"
    const val HOME = "home_graph"
    const val REGISTER = "register_graph"
    const val CHAT = "chat_graph"
    const val FEATURE = "feature_graph"
}