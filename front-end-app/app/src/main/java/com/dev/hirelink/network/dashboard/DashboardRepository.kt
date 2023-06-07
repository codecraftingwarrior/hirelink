package com.dev.hirelink.network.dashboard

import android.content.Context
import com.dev.hirelink.components.RetrofitServiceBuilder


class DashboardRepository(private val context: Context) {
    private val service: DashboardService by lazy {
        RetrofitServiceBuilder(context).create(DashboardService::class.java)
    }

    fun fetchStats() = service.fetchDashboardData()
}