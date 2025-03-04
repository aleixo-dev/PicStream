package com.nicolas.picstream.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nicolas.picstream.constants.Constants
import com.nicolas.picstream.data.remote.api.service.PhotoService
import com.nicolas.picstream.data.mapper.toDomain
import com.nicolas.picstream.data.model.Photo
import retrofit2.HttpException
import java.io.IOException

class PhotoSearchPagingSource(
    private val query: String,
    private val photoService: PhotoService
) : PagingSource<Int, Photo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {

        return try {
            val currentPage = params.key ?: 1

            val searchPhotos = photoService.searchPhoto(
                query = query,
                page = currentPage,
                perPage = Constants.PER_PAGE
            )

            LoadResult.Page(
                data = searchPhotos.photos.map { it.toDomain() },
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (searchPhotos.photos.isEmpty()) null else currentPage + 1
            )

        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Photo>) = state.anchorPosition
}