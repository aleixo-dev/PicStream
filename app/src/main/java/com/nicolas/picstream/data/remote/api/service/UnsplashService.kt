package com.nicolas.picstream.data.remote.api.service

import com.nicolas.picstream.data.response.PhotoResponse
import com.nicolas.picstream.data.response.SearchPhotoResponse
import com.nicolas.picstream.data.response.TopicResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UnsplashService {

    @GET("photos")
    suspend fun getPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage : Int,
    ) : Response<List<PhotoResponse>>

    @GET("search/photos")
    suspend fun searchPhoto(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ) : SearchPhotoResponse

    @GET("topics")
    suspend fun getTopics() : List<TopicResponse>

    @GET("topics/{slug}/photos")
    suspend fun getTopicPhotos(
        @Path("slug") slug : String,
    ) : List<PhotoResponse>
}