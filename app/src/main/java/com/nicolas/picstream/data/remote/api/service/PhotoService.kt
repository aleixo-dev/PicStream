package com.nicolas.picstream.data.remote.api.service

import com.nicolas.picstream.data.response.PagingPhotoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotoService {

    @GET("curated")
    suspend fun getPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): Response<PagingPhotoResponse>

    @GET("search")
    suspend fun searchPhoto(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): PagingPhotoResponse
}