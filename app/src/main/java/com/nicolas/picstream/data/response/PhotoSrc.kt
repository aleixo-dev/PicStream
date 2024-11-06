package com.nicolas.picstream.data.response

import kotlinx.serialization.Serializable

@Serializable
data class PhotoSrc(
    val original : String? = null,
    val large : String? = null,
    val medium : String? = null,
    val small : String? = null,
    val portrait : String? = null,
    val landscape : String? = null,
    val tiny : String? = null,
)
