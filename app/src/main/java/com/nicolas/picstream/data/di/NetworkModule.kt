package com.nicolas.picstream.data.di

import com.nicolas.picstream.BuildConfig
import com.nicolas.picstream.data.remote.api.interceptor.AuthorizationInterceptor
import com.nicolas.picstream.data.remote.api.service.PhotoService
import com.nicolas.picstream.data.repository.PhotoRepository
import com.nicolas.picstream.data.repository.PhotoRepositoryImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {

    single { provideRetrofitService() }
    single<PhotoRepository> { PhotoRepositoryImpl(get(), get()) }
}

private fun provideOkhttpClient(): OkHttpClient {

    val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }

    return OkHttpClient.Builder().apply {
        addInterceptor(AuthorizationInterceptor(BuildConfig.API_KEY))
        addInterceptor(httpLoggingInterceptor)
        connectTimeout(30, TimeUnit.SECONDS)
        readTimeout(30, TimeUnit.SECONDS)
        writeTimeout(30, TimeUnit.SECONDS)
    }.build()
}

private fun provideRetrofitService(): PhotoService {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(provideOkhttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PhotoService::class.java)
}