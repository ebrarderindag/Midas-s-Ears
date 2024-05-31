package com.example.midasproject.activity.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import com.example.midasproject.R


sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: Int
) {
    object Meeting : BottomBarScreen(
        route = "Meeting",
        title = "Toplantılar",
        icon = R.drawable.email
    )
    object Event : BottomBarScreen(
        route = "Event",
        title = "Etkinlikler",
        icon = R.drawable.email
    )
    object Birthday : BottomBarScreen(
        route = "Birthday",
        title = "Doğum Günü",
        icon = R.drawable.email
    )

}
