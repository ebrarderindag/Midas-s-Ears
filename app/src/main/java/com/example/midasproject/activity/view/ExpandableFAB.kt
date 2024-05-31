package com.example.midasproject.activity.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ExpandableFAB(
    navController: NavController,
    floatingActionButtonList: List<@Composable () -> Unit> = listOf()
) {
    var isExpanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f)

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.End
    ) {
        AnimatedVisibility(visible = isExpanded) {
            Column(
                horizontalAlignment = Alignment.End
            ) {
                for (fab in floatingActionButtonList) {
                    fab()
                }
            }
        }
        FloatingActionButton(
            onClick = { isExpanded = !isExpanded },
            containerColor = Color.White,
            contentColor = Color(0xFF9D8B74)
        ) {
            Icon(
                modifier = Modifier
                    .size(if (isExpanded) 34.dp else 30.dp)
                    .rotate(rotation),
                imageVector = if (isExpanded) Icons.Default.Close else Icons.Default.Menu,
                contentDescription = if (isExpanded) "Close" else "Add"
            )
        }
    }
}