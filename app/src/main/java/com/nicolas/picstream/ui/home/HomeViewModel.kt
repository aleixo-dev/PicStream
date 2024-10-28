package com.nicolas.picstream.ui.home

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
import com.nicolas.picstream.data.model.Topic
import com.nicolas.picstream.data.repository.PhotoRepository
import com.nicolas.picstream.utils.toCurrentDate
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
import java.util.Date

class HomeViewModel(
    private val photoRepository: PhotoRepository,
    pager: Pager<Int, PhotoEntity>,
    networkConnectivityService: NetworkConnectivityService
) : ViewModel() {

    private val _photoQueryState = MutableStateFlow<PagingData<Photo>>(PagingData.empty())
    val photoQueryState: MutableStateFlow<PagingData<Photo>> get() = _photoQueryState

    private val _photoTopicFilter = MutableStateFlow<PagingData<Photo>>(PagingData.empty())
    val photoTopicFilter: MutableStateFlow<PagingData<Photo>> get() = _photoTopicFilter

    private val _photoTopics = MutableStateFlow<List<Topic>>(emptyList())
    val photoTopics: StateFlow<List<Topic>> get() = _photoTopics.asStateFlow()

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

    fun getTopics() = viewModelScope.launch {
        photoRepository.getTopics()
            .onSuccess { _photoTopics.value = it }
            .onFailure { _photoTopics.value = emptyList() }
    }

    fun onTopic(slug: String) = viewModelScope.launch {
        photoRepository.getTopicPhotos(slug)
            .cachedIn(viewModelScope)
            .collect { topic -> _photoTopicFilter.value = topic }
    }

    fun onSearch(input: String) {
        hideKeyboard.update { false }
        _photoInputQuery.value = input
    }

    fun saveDownloadNotification(title: String, photoDescription: String) = viewModelScope.launch {
        val isDownloadNotificationSave = photoRepository.saveDownloadNotification(
            Notification(
                title = title,
                description = photoDescription,
                date = Date().toCurrentDate()
            )
        )

        _isDownloadNotification.update { isDownloadNotificationSave != -1L }
    }

    fun readAllDownloadNotification() = viewModelScope.launch {
        _isDownloadNotification.update { false }
    }

    companion object {
        const val DELAY_TEXT_CHANGE = 2000L
    }

}