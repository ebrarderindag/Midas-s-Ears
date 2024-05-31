package com.example.midasproject.activity.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.midasproject.R
import com.example.midasproject.activity.home.HomeViewModel
import com.example.midasproject.activity.signUp.SignUpViewModel
import com.example.midasproject.data.model.GetUserList
import com.example.midasproject.data.model.UserLogin
import com.example.midasproject.data.model.UsersList
import com.example.midasproject.domain.Resource

@Composable
fun LoginActivity(
    navController: NavController,
    loginViewModel: LoginViewModel = hiltViewModel(),
    signUpViewModel: SignUpViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    Login(navController, loginViewModel, signUpViewModel,homeViewModel)
    var userList by remember { mutableStateOf<Resource<GetUserList>>(Resource.Loading()) }

    LaunchedEffect(
        Unit
    ){
        userList = loginViewModel.loadUsers()
    }
    println("users: " + userList)
}


@Composable
fun Login(
    navController: NavController,
    loginViewModel: LoginViewModel = hiltViewModel(),
    signUpViewModel: SignUpViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(15.dp))
        Image(
            painter = painterResource(id = R.drawable.login_background),
            contentDescription = "Login",
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp)
                .alpha(0.4f)
                .clip(
                    CutCornerShape(
                        topStart = 8.dp,
                        topEnd = 16.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 8.dp
                    )
                )
                .background(MaterialTheme.colorScheme.background)
        )
        Column(
            Modifier
                .fillMaxSize()
                .padding(48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            LoginHeader()
            LoginFields(
                email,
                password,
                onEmailChange = { email = it },
                onPasswordChange = { password = it },
                onForgotPasswordClick = {}
            )
            LoginFooter(navController, email, password, loginViewModel, signUpViewModel, homeViewModel )
        }
    }
}

@Composable
fun LoginHeader() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.midas),
            contentDescription = "Login",
        )
        Text(
            text = "Midas' Ears",
            fontSize = 50.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF9D8B74),
            fontFamily = FontFamily.Cursive
        )
        Text(
            text = "Welcome",
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            color = Color(0xFF9D8B74)
        )
    }
}

@Composable
fun LoginFields(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        DemoField(
            value = email,
            label = "Email",
            placeholder = "Enter your email",
            onValueChange = onEmailChange,
            leadingIcon = {
                Icon(Icons.Default.Person, contentDescription = "Email")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        DemoField(
            value = password,
            label = "Password",
            placeholder = "Enter your password",
            onValueChange = onPasswordChange,
            visualTransformation = PasswordVisualTransformation(),
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = "Password")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )
        TextButton(
            onClick = { onForgotPasswordClick },
            modifier = Modifier.align(Alignment.End),
            colors = ButtonDefaults.textButtonColors(contentColor = Color.Black)
        ) {
            Text(text = "Forgot Password?")
        }
    }
}

@Composable
fun LoginFooter(
    navController: NavController,
    email: String,
    password: String,
    loginViewModel: LoginViewModel,
    signUpViewModel: SignUpViewModel,
    homeViewModel: HomeViewModel
) {
    val context = LocalContext.current


    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = {
                val login = UserLogin(email, password)
                if (login != null) {
                    loginViewModel.checkLogin(navController, context, login, email)

                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF9D8B74),
                contentColor = Color.Black
            )
        ) {
            Text(text = "Sign In")
        }
        TextButton(
            onClick = {
                signUpViewModel.signUpNav(navController, context)
            },
            colors = ButtonDefaults.textButtonColors(contentColor = Color.Black)
        ) {
            Text(text = "Don't have an account, click here")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoField(
    value: String,
    label: String,
    placeholder: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(text = label)
        },
        placeholder = {
            Text(text = placeholder)
        },
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon
    )
}



