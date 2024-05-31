package com.example.midasproject.data.repository

import com.example.midasproject.data.model.*
import com.example.midasproject.data.service.MidasService
import com.example.midasproject.domain.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class MidasRepository @Inject constructor(
    private var api : MidasService
) {
    //Login Checking
    suspend fun loginCheck(login: UserLogin) : Resource<LoginResponse> {

        val responseForLogin = try{
            api.userLogin(login)
        }catch(e: java.lang.Exception){
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(responseForLogin)
    }

    suspend fun userRegister(user : Users) : Resource<RegisterResponse> {
        val response = try{
            api.userRegister(user)
        }catch (e: java.lang.Exception){
            print("error message: "+e.message.toString())
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(response)
    }
    suspend fun addChatbox(addNewVChattbox: AddNewChattbox){
        try {
            api.addChatbox(addNewVChattbox)
        }catch (e: java.lang.Exception){
            println(e.message)
        }
    }
    suspend fun getChatbox() : Resource<Chatbox> {
        val response = try{
            api.getChatboxes()
        }catch (e: java.lang.Exception){
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(response)
    }
    // Search Event
    suspend fun searchItem(searchItem: SearchItem) : Resource<Chatbox> {
        val response = try{
            api.searchItem(searchItem.input)
        }catch (e: java.lang.Exception){
            println("Hata: " + e.message)
            return Resource.Error(e.message.toString())
        }
        println("Hata: " + response)
        return Resource.Success(response)
    }
    suspend fun getUser() : Resource<GetUserList> {
        val response = try{
            api.getUsers()
        }catch (e: java.lang.Exception){
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(response)
    }

    suspend fun getFavorite(searchFav: SearchFav) : Resource<GetFavoriteList> {
        val response = try{
            api.getFavorite(searchFav.id)
        }catch (e: java.lang.Exception){
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(response)
    }
    suspend fun addFavorite(addFavorite: addFavorite){
        try {
            api.addNewFavorite(addFavorite)
        }catch (e: java.lang.Exception){
            println(e.message)
        }
    }

    suspend fun deleteFavorite(id: Int){
        try {
            api.deleteFavorite(id)
        }catch (e: java.lang.Exception){
            println(e.message)
        }
    }

    suspend fun getAllMessage() : Resource<GetAllMessage> {
        val response = try{
            api.getAllMessage()
        }catch (e: java.lang.Exception){
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(response)
    }

    suspend fun addMessage(addMessage: AddMessage){
        try {
            api.addMessage(addMessage)
        }catch (e: java.lang.Exception){
            println(e.message)
        }
    }


}