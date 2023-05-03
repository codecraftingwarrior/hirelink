package com.dev.hirelink.network.joboffer

import com.dev.hirelink.models.JobOffer
import com.dev.hirelink.models.PaginatedResourceWrapper
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface JobOfferService {

    @GET("job-offers")
    fun findAll(@Query("page") pageNumber: Int = 1): Single<PaginatedResourceWrapper<JobOffer>>
}