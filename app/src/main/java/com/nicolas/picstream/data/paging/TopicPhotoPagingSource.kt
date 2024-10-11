package com.nicolas.picstream.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nicolas.picstream.data.mapper.toDomain
import com.nicolas.picstream.data.model.Photo
import com.nicolas.picstream.data.remote.api.service.UnsplashService
import retrofit2.HttpException

class TopicPhotoPagingSource(
    private val unsplashService: UnsplashService,
    private val query : String
) : PagingSource<Int, Photo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {

        return try {

            val currentPage = params.key ?: 1

            val topicPhotos = unsplashService.getTopicPhotos(slug = query,)
            LoadResult.Page(
                data = topicPhotos.map { it.toDomain() },
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (topicPhotos.isEmpty()) null else currentPage + 1
            )

        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        return state.anchorPosition
    }
}