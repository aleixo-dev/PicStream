package com.nicolas.picstream.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PhotoEntity(
    @PrimaryKey
    val id : String,
    val url : String,
    val alt : String,
    val photographerName : String,
)