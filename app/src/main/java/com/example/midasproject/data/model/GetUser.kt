package com.example.midasproject.data.model

data class GetUser(
    val id: Int? = null,

    val firstName: String? = null,

    val lastName: String? = null,

    val email: String? = null,

    val passwordSalt: String? = null,

    val passwordHash: String? = null,

    val gender: String? = null,

    val age: Int? = null,

    val userImage: String? = null,

    val phoneNumber: String? = null,

    val realPassword: String? = null
)
