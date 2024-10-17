package com.nicolas.picstream.connectivity

sealed interface NetworkStatus {
    data object Connected : NetworkStatus
    data object Disconnected : NetworkStatus
    data object Unknown : NetworkStatus
}