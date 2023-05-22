package com.dev.hirelink.network.jobcategory

import android.content.Context
import com.dev.hirelink.components.RetrofitServiceBuilder

class JobCategoryRepository(private val context: Context) {
    private val service: JobCategoryService by lazy {
        RetrofitServiceBuilder(context).create(JobCategoryService::class.java)
    }

    fun findAll() = service.findAll()
}