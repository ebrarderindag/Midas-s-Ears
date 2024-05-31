package com.example.midasproject.activity.navigation

sealed class RegisterNavGraph(val route: String) {
    object FirstScreen : RegisterNavGraph("first_screen")
    object SecondScreen : RegisterNavGraph("second_screen")
    object ThirdScreen : RegisterNavGraph("third_screen")

    fun withArgs(vararg args: String) : String {
        return buildString {
            append(route)
            args.forEach { arg->
                append("/$arg")
            }
        }
    }
}