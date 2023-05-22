package com.dev.hirelink.network.jobcategory

import com.dev.hirelink.models.JobOfferCategory
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers

interface JobCategoryService {

    @Headers("Accept: application/json", "Content-type: application/json")
    @GET("job-offer-categories")
    fun findAll(): Single<List<JobOfferCategory>>
}