package com.nicolas.picstream.helper

import kotlinx.coroutines.flow.Flow

interface NotificationFlag {
    val isNotificationEnable : Flow<Boolean>
}

interface DataStore : NotificationFlag {

    suspend fun toggleNotification(active: Boolean)

}