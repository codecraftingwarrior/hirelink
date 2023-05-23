package com.dev.hirelink.network.profession

import com.dev.hirelink.models.WrappedPaginatedResource
import com.dev.hirelink.models.Profession
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ProfessionService {

    @GET("professions")
    fun findAll(
        @Query("page") pageNumber: Int = 1,
        @Query("name") name: String? = null,
    ): Single<WrappedPaginatedResource<Profession>>

    @Headers("Accept: application/json", "Content-type: application/json")
    @GET("professions/all")
    fun findAllNotPaginated(): Single<List<Profession>>
}