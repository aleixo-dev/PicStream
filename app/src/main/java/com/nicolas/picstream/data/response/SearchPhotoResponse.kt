package com.nicolas.picstream.data.response

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class SearchPhotoResponse(
    @SerializedName("photos")
    val result : List<PhotoResponse>
)