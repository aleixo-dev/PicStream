package com.nicolas.picstream.data.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.nicolas.picstream.constants.Constants
import com.nicolas.picstream.data.local.database.ApplicationDatabase
import com.nicolas.picstream.data.remote.mediator.PhotoRemoteMediator
import com.nicolas.picstream.manager.DataStore
import com.nicolas.picstream.manager.NotificationFlag
import com.nicolas.picstream.manager.DataStoreManager
import com.nicolas.picstream.ui.home.HomeViewModel
import com.nicolas.picstream.ui.notifications.NotificationViewModel
import com.nicolas.picstream.ui.option.OptionViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

@OptIn(ExperimentalPagingApi::class)
val appModule = module {

    single {
        Room.databaseBuilder(
            androidApplication(),
            ApplicationDatabase::class.java,
            "photo_db"
        ).allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<ApplicationDatabase>().photoDao() }
    single { get<ApplicationDatabase>().notificationDao() }

    single {
        Pager(
            config = PagingConfig(pageSize = Constants.PER_PAGE),
            remoteMediator = PhotoRemoteMediator(
                unsplashApi = get(),
                applicationDatabase = get()
            ),
            pagingSourceFactory = { get<ApplicationDatabase>().photoDao().pagingSource() }
        )
    }

    single<DataStore> { DataStoreManager(context = androidApplication()) }
    single<NotificationFlag> { get<DataStore>() }

    viewModel {
        HomeViewModel(
            photoRepository = get(),
            pager = get(),
            networkConnectivityService = get()
        )
    }

    viewModel { OptionViewModel(dataStore = get()) }
    viewModel { NotificationViewModel(photoRepository = get()) }
}
