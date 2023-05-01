package com.dev.hirelink.network.plan

import android.content.Context
import com.dev.hirelink.components.RetrofitServiceBuilder
import com.dev.hirelink.models.Plan
import com.dev.hirelink.models.Role
import com.dev.hirelink.models.RoleType
import io.reactivex.Single

class PlanRepository(private val context: Context) {
    private val service: PlanService by lazy { RetrofitServiceBuilder(context).create(PlanService::class.java) }

    fun findAll(): Single<List<Plan>> = service.findAll()
}