package com.nicolas.picstream.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nicolas.picstream.data.mapper.toTopic
import com.nicolas.picstream.data.remote.api.service.UnsplashService
import com.nicolas.picstream.data.model.Photo
import com.nicolas.picstream.data.model.Topic
import com.nicolas.picstream.data.paging.PhotoPagingSource
import com.nicolas.picstream.data.paging.PhotoSearchPagingSource
import com.nicolas.picstream.data.paging.TopicPhotoPagingSource
import kotlinx.coroutines.flow.Flow

class PhotoRepositoryImpl(private val service: UnsplashService) : PhotoRepository {

    override suspend fun getPhotos(): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(pageSize = MAX_PAGE_SIZE, prefetchDistance = 20)
        ) {
            PhotoPagingSource(service)
        }.flow
    }

    override suspend fun searchPhoto(query: String): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(pageSize = MAX_PAGE_SIZE, prefetchDistance = 20)
        ) {
            PhotoSearchPagingSource(query, service)
        }.flow
    }

    override suspend fun getTopics(): Result<List<Topic>> {
        return runCatching {
            service.getTopics().map { it.toTopic() }
        }
    }

    override suspend fun getTopicPhotos(slug: String): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(pageSize = MAX_PAGE_SIZE, prefetchDistance = 20)
        ) {
            TopicPhotoPagingSource(service, slug)
        }.flow
    }

    companion object {
        const val MAX_PAGE_SIZE = 20
    }
}