package com.nicolas.picstream.presentation.home

import com.nicolas.picstream.utils.toCurrentDate
import java.util.Date

data class NotificationParams(
    val id : String? = null,
    val title : String? = null,
    val description : String,
    val date : String = Date().toCurrentDate(),
    val username : String,
    val url : String,
)