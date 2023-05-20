package com.dev.hirelink.network.company

import com.dev.hirelink.models.Company
import com.dev.hirelink.models.WrappedPaginatedResource
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface CompanyService {

    @GET("companies")
    fun findAll(
        @Query("page") pageNumber: Int = 1,
        @Query("name") name: String? = null,
    ): Single<WrappedPaginatedResource<Company>>
}