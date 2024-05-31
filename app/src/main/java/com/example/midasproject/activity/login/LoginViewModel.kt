package com.example.midasproject.activity.login

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.midasproject.activity.navigation.AuthScreen
import com.example.midasproject.activity.navigation.Graph
import com.example.midasproject.data.model.GetUserList
import com.example.midasproject.data.model.UserLogin
import com.example.midasproject.data.model.UsersList
import com.example.midasproject.data.repository.MidasRepository
import com.example.midasproject.domain.Resource
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject
constructor(
    private var repoLogin: MidasRepository
) : ViewModel() {

    private val _loginFlow = MutableStateFlow<Resource<UserLogin>?>(null)
    val loginFlow: StateFlow<Resource<UserLogin>?> = _loginFlow

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState


    fun checkLogin(navController: NavController, context: Context, login: UserLogin, email: String) {
        viewModelScope.launch {
            _loadingState.value = true
            val result = repoLogin.loginCheck(login)
            when (result) {
                is Resource.Success -> {
                    val response = result.data
                    if (response != null) {
                        if (!response.isSuccessfull) {
                            navController.navigate("${Graph.HOME}/${email}") {
                                popUpTo(AuthScreen.Login.route) { inclusive = true }
                            }
                            Toast.makeText(
                                context,
                                "Midas' Ears' Hoşgeldiniz...",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        } else {
                            Toast.makeText(
                                context,
                                "Lütfen, Emailinizi ve Şifrenizi kontrol edin ve tekrar deneyin.",
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
                        "Lütfen, Emailinizi ve Şifrenizi kontrol edin ve tekrar deneyin.",
                        Toast.LENGTH_LONG
                    ).show()
                    _loadingState.value = false
                }

                else -> Unit
            }
        }
    }

    suspend fun loadUsers(): Resource<GetUserList> {
        return repoLogin.getUser()
    }
}
