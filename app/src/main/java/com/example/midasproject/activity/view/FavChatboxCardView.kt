package com.example.midasproject.activity.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowCircleRight
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material.Text
import androidx.compose.foundation.border
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.example.midasproject.activity.home.HomeViewModel
import com.example.midasproject.activity.navigation.AuthScreen
import com.example.midasproject.activity.navigation.Graph
import com.example.midasproject.data.model.Chatbox
import com.example.midasproject.data.model.ChatboxItem
import com.example.midasproject.data.model.GetFavorite
import com.example.midasproject.data.model.GetUser
import com.example.midasproject.domain.Resource
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun FavChatboxCardView(
    favChatboxItem: GetFavorite,
    navController: NavController,
    onEditClicked: () -> Unit,
    homeViewModel: HomeViewModel,
    user: GetUser
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var chatbox = getChatbox(chatboxID = favChatboxItem.chattBoxId.toString(), homeViewModel = homeViewModel)

    Surface(
        color = Color.Transparent,
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .border(width = 1.dp, color = Color(0xFF9D8B74), shape = MaterialTheme.shapes.small)

    ) {
        Column(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.LightGray,
                            Color.LightGray
                        )
                    )
                )
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Column {
                    Row(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier
                                .weight(2f)
                                //.fillMaxSize()
                                .padding(4.dp),
                            verticalArrangement = Arrangement.SpaceEvenly
                            //, horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = favChatboxItem.chattBoxName,
                                style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                        Column(
                            modifier = Modifier
                                // .fillMaxSize()
                                .padding(4.dp)
                                .weight(1f), verticalArrangement = Arrangement.Center
                            // , horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(modifier = Modifier.fillMaxSize()) {
                                IconButton(
                                    onClick = {
                                        val id = favChatboxItem.id
                                        scope.launch(Dispatchers.IO) {
                                            homeViewModel.deleteFavorite(id)
                                        }
                                        Toast.makeText(
                                            context,
                                            "Favorilerden kaldırıldı",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    },
                                    modifier = Modifier
                                        .alpha(0.5f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Star,
                                        contentDescription = "Fav Button",
                                        modifier = Modifier
                                            .alpha(10f)
                                    )
                                }
                                var myCurrentLat by remember { mutableStateOf(0.0) }
                                var myCurrentLon by remember { mutableStateOf(0.0) }
                                val placeLat = chatbox?.latitude?.toDouble() // Mekan enlem değeri
                                val placeLon = chatbox?.longitude?.toDouble() // Mekan boylam değeri
                                val context = LocalContext.current
                                val fusedLocationClient = remember {
                                    LocationServices.getFusedLocationProviderClient(context)
                                }
                                val permissionLauncher = rememberLauncherForActivityResult(
                                    contract = ActivityResultContracts.RequestPermission()
                                ) { isGranted ->
                                    if (isGranted) {
                                        getLastLocation(context, fusedLocationClient) { location ->
                                            location?.let {
                                                myCurrentLat = it.latitude
                                                myCurrentLon = it.longitude
                                            }
                                        }
                                    }
                                }

                                LaunchedEffect(Unit) {
                                    if (ActivityCompat.checkSelfPermission(
                                            context,
                                            Manifest.permission.ACCESS_FINE_LOCATION
                                        ) == PackageManager.PERMISSION_GRANTED
                                    ) {
                                        getLastLocation(context, fusedLocationClient) { location ->
                                            location?.let {
                                                myCurrentLat = it.latitude
                                                myCurrentLon = it.longitude
                                            }
                                        }
                                    } else {
                                        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                                    }
                                }
                                IconButton(
                                    onClick = {


                                        val distance = haversine(
                                            myCurrentLat,
                                            myCurrentLon,
                                            placeLat,
                                            placeLon
                                        )


                                        if (distance > 1) {
                                            val deger = "0"
                                            if (chatbox != null) {
                                                navController.navigate("${Graph.CHAT}/${user.id}/${chatbox.id}/${deger}") {
                                                    popUpTo(AuthScreen.Login.route) { inclusive = true }
                                                }
                                            }
                                            println("Mekan konumu bana 1 km'den uzak.")
                                        } else {
                                            val deger = "1"
                                            if (chatbox != null) {
                                                navController.navigate("${Graph.CHAT}/${user.id}/${chatbox.id}/${deger}") {
                                                    popUpTo(AuthScreen.Login.route) { inclusive = true }
                                                }
                                            }
                                        }
                                    },
                                    modifier = Modifier.alpha(0.5f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.ArrowCircleRight,
                                        contentDescription = "More Button",
                                        modifier = Modifier.alpha(1f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun getLastLocation(
    context: Context,
    fusedLocationClient: FusedLocationProviderClient,
    callback: (Location?) -> Unit
) {
    try {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                callback(location)
            }
        } else {
            callback(null)
        }
    } catch (e: SecurityException) {
        e.printStackTrace()
        callback(null)
    }
}

fun haversine(lat1: Double, lon1: Double, lat2: Double?, lon2: Double?): Double {
    val R = 6371 // Dünya'nın yarıçapı (kilometre cinsinden)
    val dLat = Math.toRadians(lat2!! - lat1)
    val dLon = Math.toRadians(lon2!! - lon1)
    val a = sin(dLat / 2) * sin(dLat / 2) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
            sin(dLon / 2) * sin(dLon / 2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return R * c // Mesafeyi kilometre olarak döndür
}

@Composable
fun getChatbox(chatboxID: String?, homeViewModel: HomeViewModel): ChatboxItem? {
    var chatboxList by remember { mutableStateOf<Resource<Chatbox>>(Resource.Loading()) }
    var chatbox by remember { mutableStateOf<ChatboxItem?>(null) }

    LaunchedEffect(Unit) {
        chatboxList = homeViewModel.loadChatbox()
    }

    when (val result = chatboxList) {
        is Resource.Success -> {
            val chatboxListt = result.data
            if (chatboxID != null) {
                chatbox = chatboxListt?.find { it.id == chatboxID.toInt() }
            }
        }

        else -> {
            // Handle other states like loading or error if needed
        }
    }
    return chatbox
}