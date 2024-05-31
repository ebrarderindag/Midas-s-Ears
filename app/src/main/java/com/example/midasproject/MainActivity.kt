package com.example.midasproject

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.midasproject.activity.navigation.Graph
import com.example.midasproject.activity.navigation.RootNavigationGraph
import com.example.midasproject.ui.theme.MidasProjectTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MidasProjectTheme {
                RootNavigationGraph(navController = rememberNavController(), startDestination = Graph.AUTHENTICATION)

            }
        }
    }
}


