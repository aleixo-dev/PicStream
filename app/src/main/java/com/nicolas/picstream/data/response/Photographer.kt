package com.nicolas.picstream.data.response

import kotlinx.serialization.Serializable

@Serializable
data class Photographer(
    val id: String,
    val username: String,
    val name: String,
)