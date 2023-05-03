package com.dev.hirelink.network.joboffer

import android.content.Context
import com.dev.hirelink.components.RetrofitServiceBuilder

class JobOfferRepository(private val context: Context) {
    private val service: JobOfferService by lazy {
        RetrofitServiceBuilder(context).create(
            JobOfferService::class.java
        )
    }

    fun findAll(pageNumber: Int = 1) = service.findAll(pageNumber)
}