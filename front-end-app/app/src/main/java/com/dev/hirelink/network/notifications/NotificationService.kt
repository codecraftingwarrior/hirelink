package com.dev.hirelink.network.notifications

import com.dev.hirelink.models.Notification
import com.dev.hirelink.models.WrappedPaginatedResource
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NotificationService {

    @GET("notifications")
    fun findAll(
        @Query("page") page: Int? = 1
    ): Single<WrappedPaginatedResource<Notification>>
}