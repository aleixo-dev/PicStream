package com.nicolas.picstream.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.nicolas.picstream.connectivity.NetworkConnectivityService
import com.nicolas.picstream.connectivity.NetworkStatus
import com.nicolas.picstream.data.local.entity.PhotoEntity
import com.nicolas.picstream.data.mapper.toPhoto
import com.nicolas.picstream.data.model.Notification
import com.nicolas.picstream.data.model.Photo
import com.nicolas.picstream.data.repository.PhotoRepository
import com.nicolas.picstream.manager.DataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val photoRepository: PhotoRepository,
    pager: Pager<Int, PhotoEntity>,
    networkConnectivityService: NetworkConnectivityService,
    private val dataStore: DataStore
) : ViewModel() {

    private val _photoQueryState = MutableStateFlow<PagingData<Photo>>(PagingData.empty())
    val photoQueryState: MutableStateFlow<PagingData<Photo>> get() = _photoQueryState

    private val _isDownloadNotification = MutableStateFlow(false)
    val isDownloadNotification: StateFlow<Boolean> get() = _isDownloadNotification.asStateFlow()

    val networkConnectivityState: StateFlow<NetworkStatus> =
        networkConnectivityService.networkStatus
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = NetworkStatus.Unknown
            )

    val photoLocalPagingFlow = pager
        .flow
        .map { pagingData -> pagingData.map { it.toPhoto() } }
        .cachedIn(viewModelScope)


    val themeMode: StateFlow<Boolean> = dataStore.toggleTheme.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = false
    )

    var hideKeyboard = MutableStateFlow(false)
        private set

    private val _photoInputQuery = MutableStateFlow("")
    val photoState = _photoInputQuery
        .debounce(DELAY_TEXT_CHANGE)
        .flatMapLatest { input ->
            flow<HomeUiState> {
                val state = if (input.isNotBlank()) {
                    photoRepository.searchPhoto(input)
                        .cachedIn(viewModelScope)
                        .collect {
                            _photoQueryState.value = it
                            hideKeyboard.update { true }
                        }
                    HomeUiState.Success
                } else {
                    HomeUiState.Success
                }

                emit(state)
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            HomeUiState.Loading
        )

    fun onSearch(input: String) {
        hideKeyboard.update { false }
        _photoInputQuery.value = input
    }

    fun saveDownloadNotification(
        title: String, description: String, date: String, username: String, url: String
    ) = viewModelScope.launch {
        val isDownloadNotificationSave = photoRepository.saveDownloadNotification(
            Notification(
                title = title,
                description = description,
                date = date,
                username = username,
                url = url
            )
        )

        _isDownloadNotification.update { isDownloadNotificationSave != -1L }
    }

    fun toggleTheme() {
        viewModelScope.launch {
            val mode = !themeMode.value
            dataStore.toggleTheme(mode)
        }
    }

    fun readAllDownloadNotification() = viewModelScope.launch {
        _isDownloadNotification.update { false }
    }

    companion object {
        const val DELAY_TEXT_CHANGE = 2000L
    }

}