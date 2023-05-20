package com.dev.hirelink.network.joboffer

import android.content.Context
import com.dev.hirelink.components.RetrofitServiceBuilder

class JobOfferRepository(private val context: Context) {
    private val service: JobOfferService by lazy {
        RetrofitServiceBuilder(context).create(
            JobOfferService::class.java
        )
    }

    fun findAll(
        pageNumber: Int = 1,
        lat: Double? = null,
        lng: Double? = null,
        maxDistance: Double? = null,
        jobTitle: String? = null,
        minSalary: Float? =  null,
        maxSalary: Float? = null,
        fromDate: String? = null,
        toDate: String? = null,
        companyIDs: List<Int>? = null,
        professionIDs: List<Int>? = null
    ) = service.findAll(pageNumber,
        latitude = lat,
        longitude = lng,
        maxDistance = maxDistance,
        jobTitle = jobTitle,
        minSalary = minSalary,
        maxSalary = maxSalary,
        fromDate = fromDate,
        toDate = toDate,
        companyIDs = companyIDs,
        professionIDs = professionIDs
    )

    fun findById(id: Int) = service.findById(id)

    fun findByOwnerId(id: Int, pageNumber: Int = 1 ) = service.findByOwnerId(id)
}