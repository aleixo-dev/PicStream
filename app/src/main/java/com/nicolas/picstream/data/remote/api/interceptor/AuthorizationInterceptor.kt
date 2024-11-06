package com.nicolas.picstream.data.remote.api.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor (private val apiKey : String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val request = originalRequest.newBuilder()
            .header("Authorization", apiKey)
            .build()

        return chain.proceed(request)
    }
}