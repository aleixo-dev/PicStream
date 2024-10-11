package com.nicolas.picstream.data.response

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class PhotoResponse(
    @SerializedName("id")
    val id: String,
    val slug : String,
    @SerializedName("urls")
    val url: UrlResponse
)