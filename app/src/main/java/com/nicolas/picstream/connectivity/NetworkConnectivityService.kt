package com.nicolas.picstream.connectivity

import kotlinx.coroutines.flow.Flow

interface NetworkConnectivityService {
    val networkStatus: Flow<NetworkStatus>
}