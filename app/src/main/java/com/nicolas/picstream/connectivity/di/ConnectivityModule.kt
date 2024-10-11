package com.nicolas.picstream.connectivity.di

import com.nicolas.picstream.connectivity.NetworkConnectivity
import com.nicolas.picstream.connectivity.NetworkConnectivityService
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val connectivityModule = module {

    single<NetworkConnectivityService> {
        NetworkConnectivity(context = androidApplication())
    }
}