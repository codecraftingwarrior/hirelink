package com.dev.hirelink.network.dashboard

import com.dev.hirelink.dto.Dashboard
import io.reactivex.Single
import retrofit2.http.GET

interface DashboardService {

    @GET("dashboard")
    fun fetchDashboardData(): Single<Dashboard>
}