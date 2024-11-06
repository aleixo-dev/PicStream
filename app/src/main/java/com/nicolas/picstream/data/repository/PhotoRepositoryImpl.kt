package com.nicolas.picstream.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nicolas.picstream.data.local.dao.NotificationDao
import com.nicolas.picstream.data.mapper.toNotificationEntity
import com.nicolas.picstream.data.mapper.toNotificationModel
import com.nicolas.picstream.data.model.Notification
import com.nicolas.picstream.data.model.Photo
import com.nicolas.picstream.data.paging.PhotoSearchPagingSource
import com.nicolas.picstream.data.remote.api.service.PhotoService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PhotoRepositoryImpl(
    private val service: PhotoService,
    private val notificationDao: NotificationDao,
    private val coroutineScope: CoroutineDispatcher = Dispatchers.IO
) : PhotoRepository {

    override fun searchPhoto(query: String): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(pageSize = MAX_PAGE_SIZE, prefetchDistance = 20)
        ) {
            PhotoSearchPagingSource(query, service)
        }.flow
    }

    override suspend fun saveDownloadNotification(notification: Notification) =
        withContext(coroutineScope) {
            notificationDao.insertDownloadNotification(notification.toNotificationEntity())
        }

    override fun getAllNotification(): Flow<List<Notification>> {
        return notificationDao.selectAllNotification()
            .map { it.toNotificationModel() }
    }

    override suspend fun deleteAllNotification() {
        notificationDao.deleteAll()
    }

    companion object {
        const val MAX_PAGE_SIZE = 80
    }
}