package com.dev.hirelink.network.jobapplication

import com.dev.hirelink.models.JobApplication
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path

interface JobApplicationService {

    @Multipart
    @POST("job-applications")
    fun create(
        @PartMap fields: MutableMap<String, RequestBody>,
        @Part files: List<MultipartBody.Part>
    ): Single<JobApplication>

    @GET("job-applications/{id}")
    fun findById(
        @Path("id") id: Int
    ): Single<JobApplication>

    @PUT("job-applications/{id}")
    fun update(
        @Path("id") id: Int,
        @Body jobApplication: JobApplication
    ): Single<JobApplication>
}