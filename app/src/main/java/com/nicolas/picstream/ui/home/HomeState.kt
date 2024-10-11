package com.nicolas.picstream.ui.home

import com.nicolas.picstream.data.model.Topic

sealed class HomeUiState {

    data object Loading : HomeUiState()
    data object Error : HomeUiState()
    data object Success : HomeUiState()
}

sealed class TopicUiState {
    data object Loading : TopicUiState()
    data object Error : TopicUiState()
    data class Success(val topics : List<Topic>) : TopicUiState()
}
