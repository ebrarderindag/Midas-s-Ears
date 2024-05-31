package com.example.midasproject.activity.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.midasproject.data.model.AddNewChattbox
import com.example.midasproject.data.model.Chatbox
import com.example.midasproject.data.model.SearchItem
import com.example.midasproject.activity.view.SearchWidgetState
import com.example.midasproject.data.model.AddMessage
import com.example.midasproject.data.model.DeleteFavorite
import com.example.midasproject.data.model.GetAllMessage
import com.example.midasproject.data.model.GetFavoriteList
import com.example.midasproject.data.model.GetUserList
import com.example.midasproject.data.model.SearchFav
import com.example.midasproject.data.model.UsersList
import com.example.midasproject.data.model.addFavorite
import com.example.midasproject.data.repository.MidasRepository
import com.example.midasproject.domain.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject
constructor(
    private var repoChatbox: MidasRepository
) : ViewModel() {
    val didAnimationExecute = mutableStateOf(false)

    private val _loginFlow = MutableStateFlow<Resource<UsersList>?>(null)
    val loginFlow: StateFlow<Resource<UsersList>?> = _loginFlow

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState



    suspend fun loadChatbox(): Resource<Chatbox> {
        return repoChatbox.getChatbox()
    }
    suspend fun loadUsers(): Resource<GetUserList> {
        return repoChatbox.getUser()
    }
    suspend fun loadFavChatbox(fav: SearchFav): Resource<GetFavoriteList> {
        return repoChatbox.getFavorite(fav)
    }
    suspend fun searchChatbox(searchText: SearchItem): Resource<Chatbox> {
        return repoChatbox.searchItem(searchText)
    }
    suspend fun addChatbox(addNewChattbox: AddNewChattbox) {
        repoChatbox.addChatbox(addNewChattbox)
    }
    suspend fun addNewFavorite( addFavorite: addFavorite) {
        repoChatbox.addFavorite(addFavorite)
    }
    suspend fun deleteFavorite(id: Int) {
        repoChatbox.deleteFavorite(id)
    }
    suspend fun loadAllMessage(): Resource<GetAllMessage> {
        return repoChatbox.getAllMessage()
    }
    suspend fun addMessage( addMessage: AddMessage) {
        repoChatbox.addMessage(addMessage)
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _searchWidgetStateForChatbox: MutableState<SearchWidgetState> =
        mutableStateOf(value = SearchWidgetState.CLOSED)
    val searchWidgetStateForChatbox: State<SearchWidgetState> = _searchWidgetStateForChatbox

    private val _searchTextStateForChatbox: MutableState<String> = mutableStateOf(value = "")
    val searchTextStateForChatbox: State<String> = _searchTextStateForChatbox

    fun updateSearchWidgetStateForChatbox(newValue: SearchWidgetState) {
        _searchWidgetStateForChatbox.value = newValue
    }

    fun updateSearchTextStateForChatbox(newValue: String) {
        _searchTextStateForChatbox.value = newValue
    }

    fun refreshEvents(navController: NavController) {
        viewModelScope.launch {
            _isLoading.value = true
            delay(2000L)
            navController.currentDestination
            _isLoading.value = false
        }
    }
}