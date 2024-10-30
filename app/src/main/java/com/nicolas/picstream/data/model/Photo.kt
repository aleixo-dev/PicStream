package com.nicolas.picstream.data.model

data class Photo(
    val id : String,
    val url : PhotoUrl,
    val slug : String? = null,
    val photographerName : String? = null,
    val description : String? = null
)
