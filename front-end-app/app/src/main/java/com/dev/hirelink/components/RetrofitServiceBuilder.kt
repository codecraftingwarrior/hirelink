package com.dev.hirelink.components

import android.content.Context
import com.dev.hirelink.network.JWTTokenInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitServiceBuilder(private val context: Context) {
    private var client: OkHttpClient;
    private var retrofit: Retrofit


    init {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val jwtTokenInterceptor = JWTTokenInterceptor(context)

        this.client = OkHttpClient
            .Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(jwtTokenInterceptor)
            .build()

        this.retrofit = Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(Globals.API_BASE_URL)
            .client(this.client)
            .build()

    }

    fun <T> create(service: Class<T>): T {
        return retrofit.create(service);
    }

}