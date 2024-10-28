package com.nicolas.picstream.ui.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nicolas.picstream.data.model.Notification
import com.nicolas.picstream.data.repository.PhotoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val photoRepository: PhotoRepository
) : ViewModel() {

    val notifications: StateFlow<List<Notification>> = photoRepository.getAllNotification()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun clearNotification() = viewModelScope.launch {
        photoRepository.deleteAllNotification()
    }
}