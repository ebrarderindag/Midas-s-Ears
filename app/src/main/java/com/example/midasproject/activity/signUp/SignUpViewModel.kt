package com.example.midasproject.activity.signUp

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.midasproject.activity.navigation.AuthScreen
import com.example.midasproject.activity.navigation.Graph
import com.example.midasproject.activity.navigation.RegisterNavGraph
import com.example.midasproject.data.model.Users
import com.example.midasproject.data.model.UsersList
import com.example.midasproject.data.repository.MidasRepository
import com.example.midasproject.domain.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignUpViewModel @Inject constructor(
    private var repo: MidasRepository
) : ViewModel() {

    private val _loginFlow = MutableStateFlow<Resource<UsersList>?>(null)
    val loginFlow: StateFlow<Resource<UsersList>?> = _loginFlow

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    fun signUpNav(navController: NavController, context: Context) {
        navController.navigate(Graph.REGISTER) {
            popUpTo(AuthScreen.Login.route) { inclusive = true }
        }
    }

    fun signUpCheck(
        navController: NavController,
        context: Context,
        user: Users,
        navControllerSwipe: NavController,
        firstname: String,
        lastname: String,
        email: String,
        password: String,
        s: String,
        s1: String,
        s2: String
    ) {

        viewModelScope.launch {
            _loadingState.value = true
            val result = repo.userRegister(user)
            val response = result.data
            when (result) {
                is Resource.Success -> {
                    if (response != null) {
                        if (!response.isSuccessfull) {
                            Toast.makeText(context, "Kayıt Başarılı...", Toast.LENGTH_LONG).show()
                           navControllerSwipe.navigate(
                                RegisterNavGraph.ThirdScreen.withArgs(
                                    firstname,
                                    lastname,
                                    email,
                                    password,
                                    s,
                                    s1,
                                    s2,
                                    response.message
                                )
                            )
                        } else {
                            Toast.makeText(
                                context,
                                "Lütfen bilgilerinizi kontrol edin ve tekrar deneyin.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        Toast.makeText(context, "There is a error at server", Toast.LENGTH_LONG)
                            .show()
                    }
                    _loadingState.value = false

                }
                is Resource.Error -> {
                    Toast.makeText(
                        context,
                        "Lütfen bilgilerinizi kontrol edin ve tekrar deneyin.",
                        Toast.LENGTH_LONG
                    ).show()
                    _loadingState.value = false
                }
                else -> Unit
            }
        }
    }
}