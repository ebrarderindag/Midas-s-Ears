package com.example.midasproject.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.midasproject.R
import com.example.midasproject.activity.login.LoginActivity
import com.example.midasproject.ui.theme.MidasProjectTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")

    @Composable
    fun SplashActivity(navController : NavController) {
        SplashScreen(navController)
    }
    @Composable
    private fun SplashScreen(navController: NavController) {
        val alpha = remember{
            Animatable(0f)
        }
        LaunchedEffect(key1 = true){
            alpha.animateTo(1f,
            animationSpec = tween(3500)
            )
            delay(1000)
            navController.popBackStack()
            navController.navigate("auth_graph")
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color(0xFFE6E5E5)
                ), 
            contentAlignment = Alignment.Center
        ) {

            Image(
                modifier = Modifier.alpha(alpha.value) ,
                painter = painterResource(id = R.drawable.splash_midas),
                contentDescription = null
            )
        }
    }
