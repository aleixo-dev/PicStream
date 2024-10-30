package com.nicolas.picstream.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PhotoEntity(
    @PrimaryKey
    val id : String,
    val imageUrl : String,
    val slug : String,
    val photographerName : String,
    val description : String,
)