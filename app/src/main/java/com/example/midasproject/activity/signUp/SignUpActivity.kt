package com.example.midasproject.activity.signUp

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.East
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.West
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.midasproject.R
import com.example.midasproject.activity.navigation.AuthScreen
import com.example.midasproject.activity.navigation.Graph
import com.example.midasproject.activity.navigation.RegisterNavGraph
import com.example.midasproject.data.model.Users

@Composable
fun SignUpActivity(
    navController: NavController,
    signUpViewModel: SignUpViewModel = hiltViewModel()
): String {
    return ScreenSwipe(navController, signUpViewModel)
}

@Composable
fun LoginHeader() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Midas' Ears",
            fontSize = 50.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF9D8B74),
            fontFamily = FontFamily.Cursive
        )
        Text(
            text = "Sign Up",
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            color = Color(0xFF9D8B74)
        )
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


@Composable
fun ScreenSwipe(navController: NavController, signUpViewModel: SignUpViewModel): String {
    val navControllerSwipe = rememberNavController()
    var email = ""
    NavHost(
        navController = navControllerSwipe,
        startDestination = RegisterNavGraph.FirstScreen.route
    ) {

        composable(route = RegisterNavGraph.FirstScreen.route) {
            Screen1(
                navController = navController,
                navControllerSwipe = navControllerSwipe,
                firstname = "",
                email = "",
                password = "",
                lastname = "",
                age = "Age",
                gender = "0",
                picture = "0"
            )
        }
        composable(
            route = RegisterNavGraph.FirstScreen.route + "/{firstname}/{lastname}/{email}/{password}/{age}/{gender}/{picture}",
            arguments = listOf(
                navArgument("firstname") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("lastname") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("email") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("password") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("age") {
                    type = androidx.navigation.NavType.StringType
                    nullable = true
                },
                navArgument("gender") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("picture") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { entry ->
            Screen1(
                navController = navController,
                signUpViewModel = signUpViewModel,
                navControllerSwipe = navControllerSwipe,
                firstname = entry.arguments?.getString("firstname").toString(),
                lastname = entry.arguments?.getString("lastname").toString(),
                email = entry.arguments?.getString("email").toString(),
                password = entry.arguments?.getString("password").toString(),
                age = entry.arguments?.getString("age").toString(),
                gender = entry.arguments?.getString("gender").toString(),
                picture = entry.arguments?.getString("picture").toString()
            )
        }
        composable(
            route = RegisterNavGraph.SecondScreen.route + "/{firstname}/{lastname}/{email}/{password}/{age}/{gender}/{picture}",
            arguments = listOf(
                navArgument("firstname") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("lastname") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("email") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("password") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("age") {
                    type = androidx.navigation.NavType.StringType
                    nullable = true
                },
                navArgument("gender") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("picture") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { entry ->
            Screen2(
                navController = navController,
                signUpViewModel = signUpViewModel,
                navControllerSwipe = navControllerSwipe,
                firstname = entry.arguments?.getString("firstname").toString(),
                lastname = entry.arguments?.getString("lastname").toString(),
                email = entry.arguments?.getString("email").toString(),
                password = entry.arguments?.getString("password").toString(),
                age = entry.arguments?.getString("age").toString(),
                gender = entry.arguments?.getString("gender").toString(),
                picture = entry.arguments?.getString("picture").toString(),
            )
        }
        composable(
            route = RegisterNavGraph.ThirdScreen.route + "/{firstname}/{lastname}/{email}/{password}/{age}/{gender}/{picture}/{emailcode}",
            arguments = listOf(
                navArgument("firstname") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("lastname") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("email") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("password") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("age") {
                    type = androidx.navigation.NavType.StringType
                    nullable = true
                },
                navArgument("gender") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("picture") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("emailcode") {
                    type = NavType.StringType
                    nullable = true
                }

            )
        ) { entry ->
            email = Screen3(
                navController = navController,
                signUpViewModel = signUpViewModel,
                navControllerSwipe = navControllerSwipe,
                firstname = entry.arguments?.getString("firstname").toString(),
                lastname = entry.arguments?.getString("lastname").toString(),
                email = entry.arguments?.getString("email").toString(),
                password = entry.arguments?.getString("password").toString(),
                age = entry.arguments?.getString("age").toString(),
                gender = entry.arguments?.getString("gender").toString(),
                picture = entry.arguments?.getString("picture").toString(),
                realCode = entry.arguments?.getString("emailcode").toString()
            )
        }
    }
    return email
}

@Composable
fun Screen1(
    signUpViewModel: SignUpViewModel = hiltViewModel(),
    navController: NavController,
    navControllerSwipe: NavController,
    firstname: String,
    lastname: String,
    email: String,
    password: String,
    age: String,
    gender: String,
    picture: String

) {
    var firstname by remember {
        mutableStateOf(firstname)
    }
    var lastname by remember {
        mutableStateOf(lastname)
    }
    var email by remember {
        mutableStateOf(email)
    }
    var password by remember {
        mutableStateOf(password)
    }
    var password2 by remember {
        mutableStateOf(password)
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
            Screen1Ui(
                firstname,
                lastname,
                email,
                password,
                password2,
                onFirstnameChange = { firstname = it },
                onLastnameChange = { lastname = it },
                onEmailChange = { email = it },
                onPasswordChange = { password = it },
                onConfirmPasswordChange = { password2 = it },
            )
            IconButton(onClick = {
                navControllerSwipe.navigate(
                    RegisterNavGraph.SecondScreen.withArgs(
                        firstname,
                        lastname,
                        email,
                        password,
                        age,
                        gender,
                        picture
                    )
                )
            }) {
                Icon(
                    Icons.Filled.East,
                    contentDescription = "",
                    Modifier.fillMaxSize(),
                    Color.Gray
                )
            }
            IconButton(onClick = {
                navController.navigate(Graph.AUTHENTICATION) {
                    popUpTo(AuthScreen.Login.route) { inclusive = true }
                }
            }) {
                Icon(
                    Icons.Filled.Home,
                    contentDescription = "",
                    Modifier.fillMaxSize(),
                    Color.Gray
                )
            }
        }
    }
}

@Composable
fun Screen1Ui(
    firstname: String,
    lastname: String,
    email: String,
    password: String,
    confirmPassword: String,
    onFirstnameChange: (String) -> Unit,
    onLastnameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit
) {

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        DemoField(
            value = firstname,
            label = "Firstname",
            placeholder = "Enter your first name",
            onValueChange = onFirstnameChange,
            leadingIcon = {
                Icon(Icons.Default.Person, contentDescription = "First Name")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(8.dp))
        DemoField(
            value = lastname,
            label = "Lastname",
            placeholder = "Enter your last name",
            onValueChange = onLastnameChange,
            leadingIcon = {
                Icon(Icons.Default.Person, contentDescription = "Last Name")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        DemoField(
            value = email,
            label = "Email",
            placeholder = "Enter your mail",
            onValueChange = onEmailChange,
            leadingIcon = {
                Icon(Icons.Default.Email, contentDescription = "Email")
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
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        DemoField(
            value = confirmPassword,
            label = "Confirm Password",
            placeholder = "Enter your password",
            onValueChange = onConfirmPasswordChange,
            visualTransformation = PasswordVisualTransformation(),
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = "Confirm Password")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )
    }
}

@Composable
fun Screen2(
    signUpViewModel: SignUpViewModel = hiltViewModel(),
    navController: NavController,
    navControllerSwipe: NavController,
    firstname: String,
    lastname: String,
    email: String,
    password: String,
    age: String,
    gender: String,
    picture: String

) {
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize()) {
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
            Spacer(modifier = Modifier.height(50.dp))
            var screen2List = Screen2Ui(age, gender, picture)

            Spacer(modifier = Modifier.height(100.dp))
            Row(
                Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterHorizontally),
                Arrangement.Center
            ) {
                IconButton(onClick = {
                    navControllerSwipe.navigate(
                        RegisterNavGraph.FirstScreen.withArgs(
                            firstname,
                            lastname,
                            email,
                            password,
                            screen2List[0],
                            screen2List[1],
                            screen2List[2]
                        )
                    )
                }) {
                    Icon(
                        Icons.Filled.West,
                        contentDescription = "",
                        Modifier.fillMaxSize(),
                        Color.Gray
                    )
                }

                IconButton(onClick = {
                    val user = Users(
                        firstName = firstname,
                        lastName = lastname,
                        email = email,
                        passwordSalt = password,
                        age = screen2List[0].toInt(),
                        userImage = screen2List[2],
                        gender = screen2List[1],
                        phoneNumber = "05555555555"
                    )
                    if (user != null) {
                        signUpViewModel.signUpCheck(
                            navController,
                            context,
                            user,
                            navControllerSwipe,
                            firstname,
                            lastname,
                            email,
                            password,
                            screen2List[0],
                            screen2List[1],
                            screen2List[2]
                        )
                    }
                }) {
                    Icon(
                        Icons.Filled.East,
                        contentDescription = "",
                        Modifier.fillMaxSize(),
                        Color.Gray,
                    )
                }
            }
        }
    }
}

data class ToggleableGender(
    var isChecked: Boolean,
    val text: String
)

data class ToggleablePicture(
    val isChecked: Boolean,
    val text: String,
    val picture: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen2Ui(age: String, gender: String, picture: String): ArrayList<String> {
    var isExpanded by remember {
        mutableStateOf(false)
    }
    var age by remember {
        mutableStateOf(age)
    }
    var gender by remember {
        mutableStateOf(gender)
    }

    Column(
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(text = "Select Age:")
        Spacer(modifier = Modifier.height(5.dp))
        ExposedDropdownMenuBox(expanded = isExpanded, onExpandedChange = { isExpanded = true }) {
            TextField(
                value = age,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
                for (i in 12..70)
                    DropdownMenuItem(
                        text = {
                            Text(text = "$i")
                        },
                        onClick = {
                            age = i.toString()
                            isExpanded = false
                        }
                    )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Select Gender:")
        Spacer(modifier = Modifier.height(5.dp))
        Row() {
            val radioButtons = remember {
                mutableStateListOf(
                    ToggleableGender(
                        isChecked = false,
                        text = "Female"
                    ),
                    ToggleableGender(
                        isChecked = false,
                        text = "Male"
                    )
                )
            }
            radioButtons.forEachIndexed { index, info ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable {
                            radioButtons.replaceAll {
                                it.copy(
                                    isChecked = it.text == info.text
                                )
                            }
                        }
                        .padding(end = 16.dp)
                ) {
                    RadioButton(
                        selected = info.isChecked,
                        onClick = {
                            radioButtons.replaceAll {
                                it.copy(
                                    isChecked = it.text == info.text
                                )
                            }
                            gender = info.text
                        }
                    )
                    Text(text = info.text)
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        if (gender == "Female") {
            Column() {
                Text(text = "Select Picture:")
                Spacer(modifier = Modifier.height(10.dp))
                val radioButtons = remember {
                    mutableStateListOf(
                        ToggleablePicture(
                            isChecked = false,
                            text = "1",
                            picture = R.drawable.woman2
                        ),
                        ToggleablePicture(
                            isChecked = false,
                            text = "2",
                            picture = R.drawable.woman1
                        )
                    )
                }
                Row() {
                    radioButtons.forEachIndexed { index, info ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    radioButtons.replaceAll {
                                        it.copy(
                                            isChecked = it.text == info.text
                                        )
                                    }
                                }
                                .padding(end = 5.dp)
                        ) {
                            RadioButton(
                                selected = info.isChecked,
                                onClick = {
                                    radioButtons.replaceAll {
                                        it.copy(
                                            isChecked = it.text == info.text
                                        )
                                    }
                                }
                            )
                            Image(
                                painter = painterResource(id = info.picture),
                                contentDescription = "picture"
                            )
                        }
                    }
                }
            }
        }
        if (gender == "Male") {
            Column() {
                Text(text = "Select Picture:")
                Spacer(modifier = Modifier.height(10.dp))
                val radioButtons = remember {
                    mutableStateListOf(
                        ToggleablePicture(
                            isChecked = false,
                            text = "1",
                            picture = R.drawable.man2
                        ),
                        ToggleablePicture(
                            isChecked = false,
                            text = "2",
                            picture = R.drawable.man1
                        )
                    )
                }
                Row() {
                    radioButtons.forEachIndexed { index, info ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    radioButtons.replaceAll {
                                        it.copy(
                                            isChecked = it.text == info.text
                                        )
                                    }
                                }
                                .padding(end = 5.dp)
                        ) {
                            RadioButton(
                                selected = info.isChecked,
                                onClick = {
                                    radioButtons.replaceAll {
                                        it.copy(
                                            isChecked = it.text == info.text
                                        )
                                    }
                                }
                            )
                            Image(
                                painter = painterResource(id = info.picture),
                                contentDescription = "picture"
                            )
                        }
                    }
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(40.dp))
    val screen2List = ArrayList<String>()
    screen2List.add(age)
    screen2List.add(gender)
    screen2List.add(picture)
    return screen2List
}


@Composable
fun Screen3(
    signUpViewModel: SignUpViewModel = hiltViewModel(),
    navController: NavController,
    navControllerSwipe: NavController,
    firstname: String,
    lastname: String,
    email: String,
    password: String,
    age: String,
    gender: String,
    picture: String,
    realCode: String
):String {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
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
            val emailCode = Screen3Ui(email)
            Spacer(modifier = Modifier.height(50.dp))
            Row(
                Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterHorizontally),
                Arrangement.Center
            ) {
                IconButton(onClick = {
                    navControllerSwipe.navigate(
                        RegisterNavGraph.SecondScreen.withArgs(
                            firstname,
                            lastname,
                            email,
                            password,
                            age,
                            gender,
                            picture
                        )
                    )
                }) {
                    Icon(
                        Icons.Filled.West,
                        contentDescription = "",
                        Modifier.fillMaxSize(),
                        Color.Gray
                    )
                }
                IconButton(onClick = {
                    if (realCode == emailCode) {
                        Toast.makeText(context, "Kayıt Başarılı...", Toast.LENGTH_LONG).show()
                        navController.navigate(Graph.AUTHENTICATION) {
                            popUpTo(AuthScreen.Login.route) { inclusive = true }
                        }
                    }
                }) {
                    Icon(
                        Icons.Filled.Check,
                        contentDescription = "",
                        Modifier.fillMaxSize(),
                        Color.Gray,
                    )
                }
            }
        }
    }
    return email
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen3Ui(email: String?): String {
    var emailCode by remember {
        mutableStateOf("")
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Image(
            painter = painterResource(id = R.drawable.email),
            contentDescription = "Login",
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Verify Your Email",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
        )
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = "Please enter the 4 digit code send to '$email'",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray,
        )
        Spacer(modifier = Modifier.height(30.dp))
        TextField(
            value = emailCode,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "emailIcon"
                )
            },
            onValueChange = { emailCode = it },
            label = {
                Text(
                    text = "Verify Code",
                    color = Color.Gray,
                    fontWeight = FontWeight.Normal
                )
            },
            placeholder = { Text(text = "Enter your code", color = Color.Gray) }
        )
    }
    return emailCode
}



