package com.nicolas.picstream.data.response

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class SearchPhotoResponse(
    @SerializedName("results")
    val result : List<PhotoResponse>
)