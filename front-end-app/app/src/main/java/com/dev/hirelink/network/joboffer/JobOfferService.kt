package com.dev.hirelink.network.joboffer

import com.dev.hirelink.dto.WrappedJobApplication
import com.dev.hirelink.models.JobApplication
import com.dev.hirelink.models.JobOffer
import com.dev.hirelink.models.WrappedPaginatedResource
import io.reactivex.Single
import retrofit2.http.*

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
        @Query("searchQuery") searchQuery: String? = null,
    ): Single<WrappedPaginatedResource<JobOffer>>

    @POST("job-offers")
    fun create(@Body jobOffer: JobOffer): Single<JobOffer>

    @Headers("Accept: application/json", "Content-type: application/json")
    @GET("job-offers/{id}/job-applications")
    fun findJobApplicationsByJobOfferId(
        @Path("id") jobOfferId: Int
    ): Single<WrappedJobApplication>
}