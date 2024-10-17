package com.nicolas.picstream.ui.home

sealed class HomeUiState {

    data object Loading : HomeUiState()
    data object Error : HomeUiState()
    data object Success : HomeUiState()
}
