package com.nicolas.picstream.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nicolas.picstream.constants.Constants
import com.nicolas.picstream.data.remote.api.service.UnsplashService
import com.nicolas.picstream.data.mapper.toDomain
import com.nicolas.picstream.data.model.Photo
import okio.IOException
import retrofit2.HttpException

class PhotoPagingSource(
    private val unsplashService: UnsplashService
) : PagingSource<Int, Photo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        return try {
            val currentPage = params.key ?: 1
            val photos = unsplashService.getPhotos(
                page = currentPage,
                perPage = Constants.PER_PAGE
            ).body() ?: emptyList()

            LoadResult.Page(
                data = photos.map { it.toDomain() },
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (photos.isEmpty()) null else currentPage + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        return state.anchorPosition
    }
}