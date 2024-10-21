package com.nicolas.picstream.data.repository

import androidx.paging.PagingData
import com.nicolas.picstream.data.model.Photo
import com.nicolas.picstream.data.model.Topic
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {

    suspend fun searchPhoto(query : String) : Flow<PagingData<Photo>>
    suspend fun getTopics() : Result<List<Topic>>
    suspend fun getTopicPhotos(slug : String) : Flow<PagingData<Photo>>

}