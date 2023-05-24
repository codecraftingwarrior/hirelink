package com.dev.hirelink.network.notifications

import android.content.Context
import com.dev.hirelink.components.RetrofitServiceBuilder

class NotificationRepository(
    private val context: Context
) {
    private val service: NotificationService by lazy {
        RetrofitServiceBuilder(context).create(NotificationService::class.java)
    }

    fun findAll(page: Int? = 1) = service.findAll(page)
}