package com.nicolas.picstream.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PagingPhotoResponse(
    @SerialName("total_results")
    val totalResult : Int,
    val page : Int,
    @SerialName("per_page")
    val perPage : Int,
    val photos : List<PhotoResponse>,
)
