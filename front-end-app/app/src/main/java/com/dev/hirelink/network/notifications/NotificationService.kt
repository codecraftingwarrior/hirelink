package com.dev.hirelink.network.notifications

import com.dev.hirelink.dto.StatusResponse
import com.dev.hirelink.dto.UnreadNotificationResponse
import com.dev.hirelink.models.Notification
import com.dev.hirelink.models.WrappedPaginatedResource
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface NotificationService {

    @GET("notifications")
    fun findAll(
        @Query("page") page: Int? = 1,
        @Query("user") user: Int? = null,
        @Query("seen") seen: Int = 0
    ): Single<WrappedPaginatedResource<Notification>>

    @GET("notifications/unread")
    fun getUnreadNotificationCount(): Single<UnreadNotificationResponse>

    @PUT("notifications/mark-all-read")
    fun markAllAsRead(
        @Body elem: JsonObject = JsonObject()
    ): Single<StatusResponse>
}