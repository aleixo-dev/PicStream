package com.nicolas.picstream.ui.option

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nicolas.picstream.manager.DataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OptionViewModel(private val dataStore: DataStore) : ViewModel() {

    val notificationActive: StateFlow<Boolean> = dataStore.isNotificationEnable
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = true
        )

    fun toggleNotification(isActive: Boolean) = viewModelScope.launch {
        dataStore.toggleNotification(isActive)
    }
}