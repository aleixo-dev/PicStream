package com.nicolas.picstream.data.remote.api.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor (private val clientId : String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val request = originalRequest.newBuilder()
            .header("Authorization", "Client-ID $clientId")
            .build()

        return chain.proceed(request)
    }
}