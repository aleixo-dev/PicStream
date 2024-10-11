package com.nicolas.picstream.helper

import kotlinx.coroutines.flow.Flow

interface DataStore {

    val notificationActive : Flow<String>

    suspend fun toggleNotification(active: Boolean)

}