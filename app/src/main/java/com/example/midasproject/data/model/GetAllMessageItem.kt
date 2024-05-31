package com.example.midasproject.data.model

data class GetAllMessageItem(
    val id: Int,
    val userId: Int,
    val message_Content: String,
    val message_Date: String,
    val chattBoxId: Int
)
