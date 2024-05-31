package com.example.midasproject.activity.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.wear.compose.material.MaterialTheme
import com.example.midasproject.R
import com.example.midasproject.data.model.AddMessage
import com.example.midasproject.data.model.Chatbox
import com.example.midasproject.data.model.ChatboxItem
import com.example.midasproject.data.model.GetAllMessage
import com.example.midasproject.data.model.GetAllMessageItem
import com.example.midasproject.data.model.GetUser
import com.example.midasproject.data.model.GetUserList
import com.example.midasproject.domain.Resource
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ChatboxDetailActivity(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    Image(
        painter = painterResource(id = R.drawable.login_background),
        contentDescription = "Login",
        modifier = Modifier
            .fillMaxSize(),
        contentScale = ContentScale.Crop
    )

    val backStackEntry = navController.currentBackStackEntryAsState()
    val userId = backStackEntry.value?.arguments?.getString("userID")
    val chatboxID = backStackEntry.value?.arguments?.getString("chatboxID")
    val deger = backStackEntry.value?.arguments?.getString("deger")
    val user = userId?.let { getUser(userId = it, homeViewModel = homeViewModel) }



    val chatbox = getChatbox(chatboxID = chatboxID, homeViewModel = homeViewModel)

    var chatboxMessages: GetAllMessage? =
        getChatboxMessage(chatboxID = chatboxID, homeViewModel = homeViewModel)


    if (chatboxMessages != null) {
        ChatboxScreen(user, chatboxMessages, chatbox, homeViewModel, navController, deger)
    }

}

@Composable
fun ChatboxHeader() {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatboxScreen(
    user: GetUser?,
    chatboxMessages: GetAllMessage,
    chatbox: ChatboxItem?,
    homeViewModel: HomeViewModel,
    navController: NavController,
    deger: String?
) {
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ChatboxHeader()
        if (chatbox != null) {
            Text(
                text = chatbox.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            items(chatboxMessages) { message ->
                val user =
                    getUser(userId = message.userId.toString(), homeViewModel = homeViewModel)
                ChatMessageItem(message, user)
            }
        }

        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            var messageText by remember { mutableStateOf("") }
            val isEnabled = deger == "1"

            TextField(
                value = messageText,
                onValueChange = { messageText = it },
                modifier = Modifier
                    .weight(1f)
                    .background(Color.White, shape = RoundedCornerShape(20.dp)),
                placeholder = { Text(text = "Enter your message...") },
                enabled = isEnabled,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            IconButton(
                onClick = {
                    if (user != null && chatbox != null) {
                        val messageDate = getCurrentTime()
                        val addMessage = user.id?.let {
                            AddMessage(
                                chattBoxId = chatbox.id,
                                message_Content = messageText,
                                userId = it,
                                message_Date = messageDate
                            )
                        }
                        coroutineScope.launch {
                            if (addMessage != null) {
                                homeViewModel.addMessage(addMessage)
                            }
                        }
                    }
                    messageText = ""
                },
                enabled = isEnabled
            ) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "Send")
            }
        }
    }
}


@Composable
fun ChatMessageItem(message: GetAllMessageItem, user: GetUser?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        if (user != null) {
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
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.onSurface.copy(alpha = 0.2f))
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            if (user != null) {
                Text(
                    text = "${user.firstName} ${user.lastName}",
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = message.message_Content,
                style = MaterialTheme.typography.body2,
                modifier = Modifier
                    .background(Color(0xFFD1C4E9), shape = RoundedCornerShape(16.dp))
                    .padding(8.dp)
            )
        }
    }
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

@Composable
fun getUser(userId: String, homeViewModel: HomeViewModel): GetUser? {
    var userList by remember { mutableStateOf<Resource<GetUserList>>(Resource.Loading()) }
    var user by remember { mutableStateOf<GetUser?>(null) }

    LaunchedEffect(Unit) {
        userList = homeViewModel.loadUsers()
    }

    when (val result = userList) {
        is Resource.Success -> {
            val usersList = result.data
            user = usersList?.find { it.id == userId.toInt() }
        }

        else -> {
            // Handle other states like loading or error if needed
        }
    }
    return user
}

@Composable
fun getChatboxMessage(chatboxID: String?, homeViewModel: HomeViewModel): GetAllMessage? {
    var messageList by remember { mutableStateOf<Resource<GetAllMessage>>(Resource.Loading()) }
    val chatboxidd = chatboxID?.toInt()

    LaunchedEffect(Unit) {
        messageList = homeViewModel.loadAllMessage()
    }

    var chatboxMessage: GetAllMessage? = null

    when (val result = messageList) {
        is Resource.Success -> {
            val messages = result.data
            if (messages != null && chatboxID != null) {
                chatboxMessage = GetAllMessage() // Initialize chatboxMessage
                messages.filter { it.chattBoxId == chatboxidd }.forEach { message ->
                    chatboxMessage!!.add(message)
                }
            }
        }

        is Resource.Error -> {
            // Handle error state
            println("Error: ${result.message}")
        }

        is Resource.Loading -> {
            // Handle loading state if necessary
            println("Loading...")
        }
    }
    return chatboxMessage
}

fun getCurrentTime(): String {
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return current.format(formatter)
}





