package com.dev.hirelink.network.joboffer

import com.dev.hirelink.models.JobOffer
import com.dev.hirelink.models.WrappedPaginatedResource
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface JobOfferService {

    @GET("job-offers")
    fun findAll(
        @Query("page") pageNumber: Int = 1,
        @Query("latitude") latitude: Double? = null,
        @Query("longitude") longitude: Double? = null,
        @Query("maxDistance") maxDistance: Double? = null,
        @Query("jobTitle") jobTitle: String? = null,
        @Query("minSalary") minSalary: Float? = null,
        @Query("maxSalary") maxSalary: Float? = null,
        @Query("fromDate") fromDate: String? = null,
        @Query("toDate") toDate: String? = null,
        @Query("companyIDs[]") companyIDs: List<Int>? = null,
        @Query("professionIDs[]") professionIDs: List<Int>? = null,
    ): Single<WrappedPaginatedResource<JobOffer>>

    @GET("job-offers/{id}")
    fun findById(
        @Path("id") id: Int
    ): Single<JobOffer>

    @GET("job-offers/owner/{id}")
    fun findByOwnerId(
        @Path("id") id: Int,
        @Query("page") pageNumber: Int = 1,
    ): Single<WrappedPaginatedResource<JobOffer>>

    @POST("job-offers")
    fun create(@Body jobOffer: JobOffer): Single<JobOffer>
}