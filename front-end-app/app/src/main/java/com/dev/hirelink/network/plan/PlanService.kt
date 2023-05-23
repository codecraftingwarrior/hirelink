package com.dev.hirelink.network.plan

import com.dev.hirelink.models.Plan
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers

interface PlanService {

    @Headers("Accept: application/json", "Content-type: application/json")
    @GET("plans")
    fun findAll(): Single<List<Plan>>
}