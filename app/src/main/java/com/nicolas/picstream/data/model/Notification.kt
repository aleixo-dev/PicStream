package com.nicolas.picstream.data.model

data class Notification(
    val id: Int? = null,
    val title: String,
    val description: String,
    val date: String,
    val username : String,
    val url : String,
)
