package com.nicolas.picstream

import android.app.Application
import com.nicolas.picstream.connectivity.di.connectivityModule
import com.nicolas.picstream.data.di.appModule
import com.nicolas.picstream.data.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class PicStreamApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PicStreamApplication)
            androidLogger()
            modules(
                networkModule,
                appModule,
                connectivityModule
            )
        }
    }
}