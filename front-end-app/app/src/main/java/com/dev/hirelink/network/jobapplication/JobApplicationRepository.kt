package com.dev.hirelink.network.jobapplication

import android.content.Context
import com.dev.hirelink.components.RetrofitServiceBuilder
import com.dev.hirelink.models.JobApplication
import okhttp3.MultipartBody
import okhttp3.RequestBody


class JobApplicationRepository(private val context: Context) {
    private val service: JobApplicationService by lazy {
        RetrofitServiceBuilder(context).create(JobApplicationService::class.java)
    }

    fun create(fields: MutableMap<String, RequestBody>, files: List<MultipartBody.Part>) =
        service.create(fields, files)

    fun findById(id: Int) = service.findById(id)

    fun update(jobApplication: JobApplication) = service.update(jobApplication.id!!, jobApplication)

    fun findAll(page: Int = 1, state: String? = null, searchQuery: String? = null) =
        service.findAll(page, state, searchQuery)
}