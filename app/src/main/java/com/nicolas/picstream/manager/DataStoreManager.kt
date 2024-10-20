package com.nicolas.picstream.manager

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "settings")

class DataStoreManager(private val context: Context) : DataStore {

    companion object {
        val NOTIFICATION_KEY = booleanPreferencesKey("notification")
    }

    override suspend fun toggleNotification(active: Boolean) {
        context.dataStore.edit {
            it[NOTIFICATION_KEY] = active
        }
    }

    override val isNotificationEnable: Flow<Boolean>
        get() = context.dataStore.data.map { preferences -> preferences[NOTIFICATION_KEY] ?: true }

}