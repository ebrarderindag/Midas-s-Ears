package com.example.midasproject.activity.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DoorFront
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ArrowCircleRight
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.midasproject.R
import com.example.midasproject.activity.navigation.AuthScreen
import com.example.midasproject.activity.navigation.Graph
import com.example.midasproject.data.model.Chatbox
import com.example.midasproject.data.model.ChatboxItem
import com.example.midasproject.data.model.SearchItem
import com.example.midasproject.activity.view.ChatboxCardView
import com.example.midasproject.activity.view.ExpandableFAB
import com.example.midasproject.activity.view.FavChatboxCardView
import com.example.midasproject.activity.view.MainSearchBar
import com.example.midasproject.activity.view.PopUpForChatbox
import com.example.midasproject.activity.view.SearchWidgetState
import com.example.midasproject.data.model.GetFavorite
import com.example.midasproject.data.model.GetFavoriteList
import com.example.midasproject.data.model.GetUser
import com.example.midasproject.data.model.GetUserList
import com.example.midasproject.data.model.SearchFav
import com.example.midasproject.data.model.addFavorite
import com.example.midasproject.domain.Resource
import kotlinx.coroutines.launch
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlin.math.*

@Composable
fun HomeActivity(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    val email = backStackEntry.value?.arguments?.getString("email")
    val user = getLoginUser(email, homeViewModel)

    Box(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(15.dp))
        Image(
            painter = painterResource(id = R.drawable.login_background),
            contentDescription = "Login",
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        if (user != null) {
            HomeContent(navController, homeViewModel, user)
        }
    }
}

@Composable
fun getLoginUser(email: String?, homeViewModel: HomeViewModel): GetUser? {
    var userList by remember { mutableStateOf<Resource<GetUserList>>(Resource.Loading()) }
    var loginUser by remember { mutableStateOf<GetUser?>(null) }

    LaunchedEffect(Unit) {
        userList = homeViewModel.loadUsers()
    }

    when (val result = userList) {
        is Resource.Success -> {
            val usersList = result.data
            loginUser = usersList?.find { it.email == email }
        }

        else -> {
            // Handle other states like loading or error if needed
        }
    }

    return loginUser
}

@Composable
fun HomeHeader(navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Midas' Ears",
            fontSize = 50.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF9D8B74),
            fontFamily = FontFamily.Cursive
        )
        Spacer(modifier = Modifier.height(10.dp))
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeContent(navController: NavController, homeViewModel: HomeViewModel, user: GetUser) {

    var chatboxList by remember { mutableStateOf<Resource<Chatbox>>(Resource.Loading()) }

    LaunchedEffect(Unit) {
        chatboxList = homeViewModel.loadChatbox()
    }

    val tabItems = listOf(
        TabItem(
            title = "Home",
            unselectedIcon = Icons.Outlined.Home,
            selectedIcon = Icons.Filled.Home
        ),
        TabItem(
            title = "Fav",
            unselectedIcon = Icons.Outlined.Star,
            selectedIcon = Icons.Filled.Star
        ),
        TabItem(
            title = "Map",
            unselectedIcon = Icons.Outlined.Map,
            selectedIcon = Icons.Filled.Map
        ),
        TabItem(
            title = "Profile",
            unselectedIcon = Icons.Outlined.Person,
            selectedIcon = Icons.Filled.Person
        )
    )
    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }
    val pagerState = rememberPagerState() {
        tabItems.size
    }
    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }
    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            selectedTabIndex = pagerState.currentPage
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HomeHeader(navController = navController)
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { index ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (tabItems[index].title == "Home") {
                    Home(navController = navController, homeViewModel = homeViewModel, user)
                }
                if (tabItems[index].title == "Fav") {
                    Favorite(navController = navController, homeViewModel = homeViewModel, user)
                }
                if (tabItems[index].title == "Map") {
                    MapScreen(
                        navController = navController,
                        homeViewModel = homeViewModel,
                        user
                    )
                }
                if (tabItems[index].title == "Profile") {
                    Profile(navController = navController, homeViewModel = homeViewModel, user)
                }
            }
        }
        TabRow(
            selectedTabIndex = selectedTabIndex,
            Modifier.background(Color.LightGray),
            contentColor = Color.Black
        ) {
            tabItems.forEachIndexed { index, item ->
                Tab(
                    selected = index == selectedTabIndex,
                    onClick = {
                        selectedTabIndex = index
                    },
                    icon = {
                        Icon(
                            imageVector = if (index == selectedTabIndex) {
                                item.selectedIcon
                            } else item.unselectedIcon,
                            contentDescription = item.title,
                            tint = Color(0xFF9D8B74)
                        )
                    },
                    modifier = Modifier.background(Color.White)
                )
            }
        }
    }
}

data class TabItem(
    val title: String,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector
)

@Composable
fun Profile(navController: NavController, homeViewModel: HomeViewModel, user: GetUser) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(
                color = Color(0x80FFFFFF), // Saydam beyaz
                shape = RoundedCornerShape(16.dp) // Kenarları yuvarlak
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Profil",
                fontSize = 30.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF9D8B74),
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.height(50.dp))

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Gray, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                val painter = when (user.userImage) {
                    "R.drawable.woman2" -> painterResource(id = R.drawable.woman2)
                    "R.drawable.woman1" -> painterResource(id = R.drawable.woman1)
                    "R.drawable.man1" -> painterResource(id = R.drawable.man1)
                    "R.drawable.man2" -> painterResource(id = R.drawable.man2)
                    else -> null
                }
                if (painter != null) {
                    Image(
                        painter = painter,
                        contentDescription = "Profil Fotoğrafı",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape), // Profil fotoğrafını yuvarlak yapar
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            val name = "${user.firstName} ${user.lastName}"

            Text(
                text = name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.height(50.dp))

            val buttonModifier = Modifier
                .fillMaxWidth()
                .height(50.dp)

            Button(
                onClick = { /* Bilgileri güncelle aksiyonu */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF9D8B74),
                    contentColor = Color.Black
                ),
                modifier = buttonModifier
            ) {
                Text(text = "BILGILERI GUNCELLE")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* Profil fotoğrafı güncelle aksiyonu */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF9D8B74),
                    contentColor = Color.Black
                ),
                modifier = buttonModifier
            ) {
                Text(text = "PROFIL FOTOGRAFI GUNCELLE")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* Açılan kuyular aksiyonu */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF9D8B74),
                    contentColor = Color.Black
                ),
                modifier = buttonModifier
            ) {
                Text(text = "ACILAN KUYULAR")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* Şifre sıfırlama aksiyonu */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF9D8B74),
                    contentColor = Color.Black
                ),
                modifier = buttonModifier
            ) {
                Text(text = "SIFRE SIFIRLAMA")
            }
        }
    }
}

//Map ekranı
@Composable
fun MapScreen(
    navController: NavController,
    homeViewModel: HomeViewModel,
    user: GetUser
) {
    var chatboxList by remember { mutableStateOf<Resource<Chatbox>>(Resource.Loading()) }

    LaunchedEffect(Unit) {
        chatboxList = homeViewModel.loadChatbox()
    }
    var favChatboxList by remember { mutableStateOf<Resource<GetFavoriteList>>(Resource.Loading()) }

    LaunchedEffect(Unit) {
        favChatboxList = user.id?.let { SearchFav(it) }?.let { homeViewModel.loadFavChatbox(it) }!!
    }
    val favoriteList = favChatboxList.data
    val selectedChatbox = remember { mutableStateOf<ChatboxItem?>(null) }
    val googleMapState = remember { mutableStateOf<GoogleMap?>(null) }

    Column {
        Spacer(modifier = Modifier.height(15.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.7f) // Harita %70
        ) {
            Map(
                navController = navController,
                homeViewModel = homeViewModel,
                chatboxList = chatboxList,
                onMarkerClick = { chatbox ->
                    selectedChatbox.value = chatbox
                    googleMapState.value?.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(chatbox.latitude.toDouble(), chatbox.longitude.toDouble()),
                            15f
                        )
                    )
                },
                onMapReady = { googleMap ->
                    googleMapState.value = googleMap
                }
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.3f) // Bilgi kutusu %30
                .padding(16.dp)
        ) {
            selectedChatbox.value?.let { chatbox ->
                MarkerInfoBox(chatbox, homeViewModel, navController, favoriteList, user)
            }
        }
    }
}

@Composable
fun Map(
    navController: NavController,
    homeViewModel: HomeViewModel,
    chatboxList: Resource<Chatbox>,
    onMarkerClick: (ChatboxItem) -> Unit,
    onMapReady: (GoogleMap) -> Unit
) {
    val mapView = rememberMapViewWithLifecycle()
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    AndroidView(
        factory = { mapView },
        update = { mapView ->
            mapView.getMapAsync { googleMap ->
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    googleMap.isMyLocationEnabled = true
                    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                        location?.let {
                            val currentLatLng = LatLng(it.latitude, it.longitude)
                            googleMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    currentLatLng,
                                    15f
                                )
                            )
                        }
                    }
                } else {
                    // Gerekli izinleri istemek için kod eklenebilir
                }
                setupMap(googleMap, chatboxList, context, onMarkerClick)
                onMapReady(googleMap)
            }
        }
    )
}

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> throw IllegalStateException()
            }
        }
        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer) }
    }
    return mapView
}

@SuppressLint("PotentialBehaviorOverride")
fun setupMap(
    googleMap: GoogleMap,
    chatboxList: Resource<Chatbox>,
    context: Context,
    onMarkerClick: (ChatboxItem) -> Unit
) {
    googleMap.uiSettings.isZoomControlsEnabled = true
    googleMap.uiSettings.isMyLocationButtonEnabled = true

    if (chatboxList is Resource.Success) {
        chatboxList.data?.forEach { chatbox ->
            try {
                val latitude = chatbox.latitude.toDouble()
                val longitude = chatbox.longitude.toDouble()
                val position = LatLng(latitude, longitude)
                val marker = googleMap.addMarker(
                    MarkerOptions().position(position).title("Chatbox ${chatbox.id}")
                )
                marker?.tag = chatbox
            } catch (e: NumberFormatException) {
                Log.e("MapError", "Invalid latitude or longitude for chatbox ${chatbox.id}")
            }
        }

        googleMap.setOnMarkerClickListener { marker ->
            val chatbox = marker.tag as? ChatboxItem
            chatbox?.let {
                onMarkerClick(it)
                googleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(it.latitude.toDouble(), it.longitude.toDouble()),
                        15f
                    )
                )
            }
            true
        }
    }
}

@Composable
fun MarkerInfoBox(
    chatbox: ChatboxItem,
    homeViewModel: HomeViewModel,
    navController: NavController,
    favoriteList: GetFavoriteList?,
    user: GetUser
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.LightGray)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = chatbox.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Created by: ${chatbox.createdPersonId}", fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Date: ${chatbox.createdDate}", fontSize = 14.sp)
        }
        Row {
            IconButton(
                onClick = {
                    val isFavorite = favoriteList?.any { it.chattBoxId == chatbox.id }
                    if (isFavorite == true) {
                        val favoriteItem = favoriteList?.find { it.chattBoxId == chatbox.id }
                        favoriteItem?.let { fav ->
                            val id = fav.id
                            scope.launch(Dispatchers.IO) {
                                homeViewModel.deleteFavorite(id)
                            }
                        }
                        Toast.makeText(
                            context,
                            "Favorilerden kaldırıldı",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val newFav = user.firstName?.let {
                            user.id?.let { userID ->
                                addFavorite(
                                    chattBoxId = chatbox.id,
                                    chattBoxName = chatbox.title,
                                    userID = userID,
                                    userName = it
                                )
                            }
                        }
                        scope.launch(Dispatchers.IO) {
                            if (newFav != null) {
                                homeViewModel.addNewFavorite(newFav)
                            }
                        }
                        Toast.makeText(
                            context,
                            "Favorilere eklendi",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                },
                modifier = Modifier.alpha(0.5f)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Star,
                    contentDescription = "Fav Button",
                    modifier = Modifier.alpha(1f)
                )
            }
            var myCurrentLat by remember { mutableStateOf(0.0) }
            var myCurrentLon by remember { mutableStateOf(0.0) }
            val placeLat = chatbox.latitude.toDouble() // Mekan enlem değeri
            val placeLon = chatbox.longitude.toDouble() // Mekan boylam değeri
            val context = LocalContext.current
            val fusedLocationClient =
                remember { LocationServices.getFusedLocationProviderClient(context) }
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
                    val distance = haversine(myCurrentLat, myCurrentLon, placeLat, placeLon)
                    if (distance > 1) {
                        val deger = "0"
                        navController.navigate("${Graph.CHAT}/${user.id}/${chatbox.id}/${deger}") {
                            popUpTo(AuthScreen.Login.route) { inclusive = true }
                        }
                        println("Mekan konumu bana 1 km'den uzak.")
                    } else {
                        val deger = "1"
                        navController.navigate("${Graph.CHAT}/${user.id}/${chatbox.id}/${deger}") {
                            popUpTo(AuthScreen.Login.route) { inclusive = true }
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

fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val R = 6371 // Dünya'nın yarıçapı (kilometre cinsinden)
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = sin(dLat / 2) * sin(dLat / 2) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
            sin(dLon / 2) * sin(dLon / 2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return R * c // Mesafeyi kilometre olarak döndür
}

//Favori Ekranı
@Composable
fun Favorite(navController: NavController, homeViewModel: HomeViewModel, user: GetUser) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Favoriler",
            fontSize = 30.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF9D8B74),
            fontFamily = FontFamily.Monospace
        )
        Spacer(modifier = Modifier.height(15.dp))
        FavScreen(navController, homeViewModel, user)
    }
}

@Composable
fun FavScreen(navController: NavController, homeViewModel: HomeViewModel, user: GetUser) {
    var favChatboxList by remember { mutableStateOf<Resource<GetFavoriteList>>(Resource.Loading()) }

    LaunchedEffect(Unit) {
        favChatboxList = user.id?.let { SearchFav(it) }?.let { homeViewModel.loadFavChatbox(it) }!!
    }
    when (favChatboxList) {
        is Resource.Success -> {
            val favChatboxx = favChatboxList.data
            if (favChatboxx != null) {
                FavChatboxListing(
                    favChatboxList = favChatboxx,
                    navController = navController,
                    homeViewModel = homeViewModel,
                    user = user
                )
            }
        }

        is Resource.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Gösterilecek Bir Etkinlik Yok")
            }
        }

        is Resource.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    color = Color.DarkGray,
                )
            }
        }
    }
}

@Composable
fun FavChatboxListing(
    navController: NavController,
    favChatboxList: GetFavoriteList,
    homeViewModel: HomeViewModel,
    user: GetUser
) {
    var selectedEvent by remember { mutableStateOf<GetFavorite?>(null) }

    LazyColumn(contentPadding = PaddingValues(5.dp)) {
        items(favChatboxList) { favChatbox ->
            FavChatboxCardView(
                favChatboxItem = favChatbox,
                navController = navController,
                onEditClicked = { selectedEvent = favChatbox },
                homeViewModel = homeViewModel,
                user = user
            )
        }
    }
}

//Kuyu Liste ekranı
@Composable
fun Home(
    navController: NavController,
    homeViewModel: HomeViewModel,
    user: GetUser
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Kuyular",
            fontSize = 30.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF9D8B74),
            fontFamily = FontFamily.Monospace
        )
        Spacer(modifier = Modifier.height(15.dp))

        SearchBar(homeViewModel, navController, user)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(homeViewModel: HomeViewModel, navController: NavController, user: GetUser) {
    val searchWidgetState by homeViewModel.searchWidgetStateForChatbox
    val searchTextState by homeViewModel.searchTextStateForChatbox
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var chatbox by remember { mutableStateOf<Resource<Chatbox>>(Resource.Loading()) }
    var showChatboxPopup by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        chatbox = homeViewModel.loadChatbox()
    }

    var favChatboxList by remember { mutableStateOf<Resource<GetFavoriteList>>(Resource.Loading()) }

    LaunchedEffect(Unit) {
        favChatboxList = user.id?.let { SearchFav(it) }?.let { homeViewModel.loadFavChatbox(it) }!!
    }
    var favoriteList = favChatboxList.data



    Scaffold(
        topBar = {
            MainSearchBar(
                searchWidgetState = searchWidgetState,
                searchTextState = searchTextState,
                onTextChange = {
                    homeViewModel.updateSearchTextStateForChatbox(newValue = it)
                },
                onCloseClicked = {
                    homeViewModel.updateSearchWidgetStateForChatbox(newValue = SearchWidgetState.CLOSED)
                },
                onSearchClicked = {
                    chatbox = Resource.Loading()
                    scope.launch {
                        chatbox = homeViewModel.searchChatbox(SearchItem(it))
                        println("search chatbox: " + chatbox)
                    }
                },
                onSearchTriggered = {
                    homeViewModel.updateSearchWidgetStateForChatbox(newValue = SearchWidgetState.OPENED)
                },
                text = "Kuyular"
            )
        },
        floatingActionButton = {
            ExpandableFAB(
                navController = navController,
                floatingActionButtonList = listOf {
                    ExtendedFloatingActionButton(
                        text = { Text("Kuyu Ekle") },
                        icon = { Icon(Icons.Default.Add, contentDescription = "Add") },
                        onClick = { showChatboxPopup = true }, // Trigger the popup
                        modifier = Modifier.padding(bottom = 10.dp),
                        contentColor = Color(0xFF9D8B74),
                        containerColor = Color.White
                    )
                    ExtendedFloatingActionButton(
                        text = { Text("Çıkış yap") },
                        icon = { Icon(Icons.Default.DoorFront, contentDescription = "Çıkış") },
                        onClick = {
                            navController.navigate(Graph.AUTHENTICATION) {
                                popUpTo(AuthScreen.Login.route) { inclusive = true }
                            }
                        }, // Trigger the popup
                        modifier = Modifier.padding(bottom = 10.dp),
                        contentColor = Color(0xFF9D8B74),
                        containerColor = Color.White
                    )
                }
            )
        }
    ) {
        val isLoading by homeViewModel.isLoading.collectAsState()

        val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = { homeViewModel.refreshEvents(navController) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFD8D3CD))
                    .padding(it)
            ) {
                Spacer(
                    modifier = Modifier
                        .padding()
                        .height(16.dp)
                )
                when (chatbox) {
                    is Resource.Success -> {
                        val chatboxx = chatbox.data
                        if (chatboxx != null) {
                            ChatboxListing(
                                navController,
                                chatboxx,
                                homeViewModel,
                                user,
                                favoriteList
                            )
                        }
                    }

                    is Resource.Error -> {
                        Toast.makeText(context, "Gösterilecek Bir Etkinlik Yok", Toast.LENGTH_SHORT)
                            .show()
                    }

                    is Resource.Loading -> {
                        CircularProgressIndicator(
                            color = Color.DarkGray,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }
    }

    // Show PopUpForChatbox if showChatboxPopup is true
    if (showChatboxPopup) {
        PopUpForChatbox(
            onDismiss = { showChatboxPopup = false },
            onAdd = { newChatbox ->
                scope.launch(Dispatchers.IO) {
                    homeViewModel.addChatbox(addNewChattbox = newChatbox)
                    homeViewModel.refreshEvents(navController)
                }
                showChatboxPopup = false
            }, homeViewModel, user
        )
    }
}

@Composable
fun ChatboxListing(
    navController: NavController,
    chatboxList: Chatbox,
    homeViewModel: HomeViewModel = hiltViewModel(),
    user: GetUser,
    favoriteList: GetFavoriteList?
) {

    var selectedEvent by remember { mutableStateOf<ChatboxItem?>(null) }


    LazyColumn(contentPadding = PaddingValues(5.dp)) {
        items(chatboxList) { chatbox ->
            chatbox.title?.let {
                chatbox.createdDate?.let { it1 ->
                    chatbox.createdPersonId?.let { it2 ->
                        ChatboxCardView(
                            user = user,
                            navController = navController,
                            onEditClicked = { selectedEvent = chatbox },
                            homeViewModel = homeViewModel,
                            favoriteList = favoriteList,
                            chatbox = chatbox
                        )
                    }
                }
            }

        }
    }
}









