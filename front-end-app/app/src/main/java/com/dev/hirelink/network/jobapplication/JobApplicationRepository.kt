package com.dev.hirelink.network.jobapplication

import android.content.Context
import com.dev.hirelink.components.RetrofitServiceBuilder
import okhttp3.MultipartBody
import okhttp3.RequestBody


class JobApplicationRepository(private val context: Context) {
    private val service: JobApplicationService by lazy {
        RetrofitServiceBuilder(context).create(JobApplicationService::class.java)
    }

    fun create(fields: MutableMap<String, RequestBody>, files: List<MultipartBody.Part>) = service.create(fields, files)
}