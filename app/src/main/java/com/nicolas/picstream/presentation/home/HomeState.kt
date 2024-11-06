package com.nicolas.picstream.presentation.home

sealed class HomeUiState {

    data object Loading : HomeUiState()
    data object Error : HomeUiState()
    data object Success : HomeUiState()
}
