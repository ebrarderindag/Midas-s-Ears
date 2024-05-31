package com.example.midasproject.data.service

import com.example.midasproject.data.model.*
import retrofit2.http.*

interface MidasService {

    //Login
    @POST("api/Auth/UserLogin")
    suspend fun userLogin(@Body login :UserLogin) : LoginResponse

    //Register
    @POST("api/Auth/UserRegister")
    suspend fun userRegister(@Body user :Users) : RegisterResponse

    //Chatbox
    @GET("api/ChattBox/GetAllChattBoxes")
    suspend fun getChatboxes() : Chatbox

    @POST("api/ChattBox/AddNewChattBox")
    suspend fun addChatbox(@Body addNewChattbox: AddNewChattbox)

    @GET("api/ChattBox/SearchGivenInput")
    suspend fun searchItem(@Query("input") input: String) : Chatbox

    @GET("api/Auth/UsersList")
    suspend fun getUsers() : GetUserList

    @POST("api/Favorite/GetFavorite")
    suspend fun getFavorite(@Query("id") id: Int) : GetFavoriteList

    @POST("api/Favorite/AddNewFavorite")
    suspend fun addNewFavorite(@Body addFavorite: addFavorite)

    @POST("api/Favorite/DeleteFavorite")
    suspend fun deleteFavorite(@Query("id") id: Int)

    @POST("api/Message/ListAllMessage")
    suspend fun getAllMessage() : GetAllMessage

    @POST("api/Message/AddMessage")
    suspend fun addMessage(@Body addMessage: AddMessage)

}