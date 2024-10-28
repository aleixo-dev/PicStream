package com.nicolas.picstream.data.repository

import androidx.paging.PagingData
import com.nicolas.picstream.data.model.Notification
import com.nicolas.picstream.data.model.Photo
import com.nicolas.picstream.data.model.Topic
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {

    suspend fun getTopics() : Result<List<Topic>>
    fun searchPhoto(query : String) : Flow<PagingData<Photo>>
    fun getTopicPhotos(slug : String) : Flow<PagingData<Photo>>

    suspend fun saveDownloadNotification(notification: Notification) : Long

    fun getAllNotification() : Flow<List<Notification>>

    suspend fun deleteAllNotification()

}