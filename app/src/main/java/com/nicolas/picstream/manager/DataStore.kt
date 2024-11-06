package com.nicolas.picstream.manager

import kotlinx.coroutines.flow.Flow

interface NotificationFlag {
    val isNotificationEnable: Flow<Boolean>
}

interface DataStore : NotificationFlag {

    val toggleTheme: Flow<Boolean>

    suspend fun toggleNotification(active: Boolean)
    suspend fun toggleTheme(mode: Boolean)

}