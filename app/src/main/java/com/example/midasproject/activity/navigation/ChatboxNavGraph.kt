package com.example.midasproject.activity.navigation

sealed class ChatboxNavGraph(val route: String) {
    object HomeScreen : ChatboxNavGraph("home_screen")
    object FavScreen : ChatboxNavGraph("fav_screen")
    object MapScreen : ChatboxNavGraph("map_screen")
    object ChatScreen : ChatboxNavGraph("chat_screen")

    fun withArgs(vararg args: String) : String {
        return buildString {
            append(route)
            args.forEach { arg->
                append("/$arg")
            }
        }
    }
}