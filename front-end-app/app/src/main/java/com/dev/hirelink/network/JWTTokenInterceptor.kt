package com.dev.hirelink.network

import okhttp3.Interceptor
import okhttp3.Response

class JWTTokenInterceptor(private val token: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        if (original.url.encodedPath.contains("/login-check") || original.url.encodedPath.contains("/auth")) {
            return chain.proceed(original)
        }

        val originalHttpUrl = original.url
        val requestBuilder = original
            .newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .url(originalHttpUrl)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}