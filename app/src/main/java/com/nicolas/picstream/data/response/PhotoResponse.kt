package com.nicolas.picstream.data.response

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class PhotoResponse(
    val id: String,
    val src : PhotoSrc,
    val photographer : String? = null,
    val alt : String? = null,
)