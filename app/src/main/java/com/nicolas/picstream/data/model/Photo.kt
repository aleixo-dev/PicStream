package com.nicolas.picstream.data.model

data class Photo(
    val id : String,
    val url : String,
    val photographerName : String? = null,
    val alt : String,
)
