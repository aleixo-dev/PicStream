package com.nicolas.picstream.data.remote.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.nicolas.picstream.data.local.database.PhotoDatabase
import com.nicolas.picstream.data.local.entity.PhotoEntity
import com.nicolas.picstream.data.mapper.toPhotoEntity
import com.nicolas.picstream.data.remote.api.service.UnsplashService
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PhotoRemoteMediator(
    private val unsplashApi: UnsplashService,
    private val photoDatabase: PhotoDatabase
) : RemoteMediator<Int, PhotoEntity>() {

    private var currentPage = 1

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, PhotoEntity>
    ): MediatorResult {

        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> {
                    currentPage = 1
                    currentPage
                }

                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )

                LoadType.APPEND -> {

                    state.lastItemOrNull()
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = true
                        )

                    currentPage += 1
                    currentPage
                }
            }

            val response = unsplashApi.getPhotos(
                page = loadKey,
                perPage = ITEMS_PER_PAGE
            )

            photoDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    photoDatabase.photoDao().clearAll()
                }

                val photoEntities = response.body()?.map { it.toPhotoEntity() }
                photoEntities?.let {
                    photoDatabase.photoDao().insert(photoEntities)
                }
            }

            MediatorResult.Success(
                endOfPaginationReached = response.body().isNullOrEmpty()
            )
        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            MediatorResult.Error(exception)
        }
    }

    companion object {
        const val ITEMS_PER_PAGE = 20
    }
}