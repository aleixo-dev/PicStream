package com.nicolas.picstream.data.response

import kotlinx.serialization.Serializable

@Serializable
data class TopicResponse(
    val id: String,
    val slug: String,
    val title: String,
    val status: String
)