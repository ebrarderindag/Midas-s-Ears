package com.example.midasproject.data.model

import com.google.type.DateTime
import java.time.LocalDateTime

data class AddNewChattbox(
    val createdPersonId: Int,
    val title: String,
    val latitude: String,
    val longitude: String,
    val createdDate: String
)
