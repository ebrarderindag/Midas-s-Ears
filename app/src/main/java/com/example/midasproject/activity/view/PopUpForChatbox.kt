package com.example.midasproject.activity.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.example.midasproject.activity.home.HomeViewModel
import com.example.midasproject.data.model.AddNewChattbox
import com.example.midasproject.data.model.GetUser
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun PopUpForChatbox(
    onDismiss: () -> Unit,
    onAdd: (AddNewChattbox) -> Unit,
    homeViewModel: HomeViewModel,
    user: GetUser
) {
    var chatboxName by remember { mutableStateOf("") }
    var chatboxLocation by remember { mutableStateOf("") }
    var chatboxTime by remember { mutableStateOf(getCurrentTime()) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        chatboxTime = getCurrentTime()
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Yeni Kuyu Aç") },
        text = {
            Column {
                // Chatbox Name
                TextFieldForNewChatbox(
                    label = "Kuyu Adı",
                    value = chatboxName,
                    inputType = InputTypeForNewChatbox.NewChatboxName
                ) { chatboxName = it }

                // Lokasyon (Read-only)
                TextFieldForNewChatbox(
                    label = chatboxLocation,
                    value = chatboxLocation,
                    inputType = InputTypeForNewChatbox.NewChatboxLocation,
                    multiline = true,
                    modifier = Modifier.height(90.dp),
                    readOnly = true,
                ) {}

                Button(
                    onClick = {
                        if (requestLocationPermissionIfNeeded(context)) {
                            coroutineScope.launch {
                                val location = getCurrentLocation(context)
                                if (location != null) {
                                    chatboxLocation = location
                                } else {
                                    Toast.makeText(context, "Konum alınamadı", Toast.LENGTH_LONG)
                                        .show()
                                }
                            }
                        } else {
                            Toast.makeText(context, "Konum izni gerekli", Toast.LENGTH_LONG).show()
                        }
                    }
                ) {
                    Text("Konum Al")
                }

                // Zaman (Read-only)
                TextFieldForNewChatbox(
                    label = chatboxTime,
                    value = chatboxTime,
                    inputType = InputTypeForNewChatbox.NewChatboxTime,
                    multiline = true,
                    modifier = Modifier.height(90.dp),
                    readOnly = true
                ) {}
            }
        },
        containerColor = Color.LightGray,
        confirmButton = {
            TextButton(
                onClick = {
                    try {
                        val locationParts = chatboxLocation.split(", ")
                        if (locationParts.size == 2) {
                            val latitude = locationParts[0].split(": ")[1]
                            val longitude = locationParts[1].split(": ")[1]
                            val newChatbox = user.id?.let {
                                AddNewChattbox(
                                    createdPersonId = it,
                                    title = chatboxName,
                                    latitude = latitude,
                                    longitude = longitude,
                                    createdDate = chatboxTime
                                )
                            }
                            if (newChatbox != null) {
                                onAdd(newChatbox)
                            }
                        } else {
                            Toast.makeText(context, "Konum formatı hatalı", Toast.LENGTH_LONG)
                                .show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Hata: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                    onDismiss()
                }
            ) {
                Text("Ekle")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("İptal Et")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldForNewChatbox(
    label: String,
    value: String,
    inputType: InputTypeForNewChatbox,
    multiline: Boolean = false,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    onValueChange: (String) -> Unit
) {
    var fieldValue by remember { mutableStateOf(value) }

    TextField(
        value = fieldValue,
        onValueChange = {
            fieldValue = it
            onValueChange(it)
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(1.dp)
            .focusRequester(focusRequester = FocusRequester()),
        label = { Text(text = label) },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        singleLine = !multiline,
        keyboardOptions = inputType.keyboardOptions,
        visualTransformation = inputType.visualTransformation,
        readOnly = readOnly
    )
}

sealed class InputTypeForNewChatbox(
    val label: String,
    val keyboardOptions: KeyboardOptions,
    val visualTransformation: VisualTransformation
) {
    object NewChatboxName : InputTypeForNewChatbox(
        label = "Kuyu Adı",
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        visualTransformation = VisualTransformation.None
    )

    object NewChatboxLocation : InputTypeForNewChatbox(
        label = "Kuyu Lokasyonu",
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        visualTransformation = VisualTransformation.None
    )

    object NewChatboxTime : InputTypeForNewChatbox(
        label = "Kuyu Açılma Saati",
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        visualTransformation = VisualTransformation.None
    )
}

@SuppressLint("MissingPermission")
suspend fun getCurrentLocation(context: Context): String {
    return try {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val location = fusedLocationClient.lastLocation.await()
        if (location != null) {
            "Lat: ${location.latitude}, Lng: ${location.longitude}"
        } else {
            "Konum alınamadı"
        }
    } catch (securityException: SecurityException) {
        "Konum izni yok"
    } catch (e: Exception) {
        "Konum alınamadı"
    }
}

fun requestLocationPermissionIfNeeded(context: Context): Boolean {
    return if (isLocationPermissionGranted(context)) {
        true
    } else {
        requestLocationPermission(context)
        false
    }
}

private fun isLocationPermissionGranted(context: Context): Boolean {
    return ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

private fun requestLocationPermission(context: Context) {
    ActivityCompat.requestPermissions(
        context as Activity,
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ),
        LOCATION_PERMISSION_REQUEST_CODE
    )
}

// İzinlerin sonuçlarını işlemek için requestCode tanımlanmalıdır
// Bu kodu, uygulamanın başka bir yerinde (örneğin bir Activity veya Fragment'ta) kullanabilirsiniz.
const val LOCATION_PERMISSION_REQUEST_CODE = 123 // Herhangi bir değer olabilir, bu sadece bir örnektir.


fun getCurrentTime(): String {
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss")
    return current.format(formatter)
}
