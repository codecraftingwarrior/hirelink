package com.dev.hirelink.network.user

import android.content.Context
import com.dev.hirelink.components.RetrofitServiceBuilder
import com.dev.hirelink.dto.EmployerChoosePlanRequest
import com.dev.hirelink.network.role.RoleService

class UserRepository(private val context: Context) {
    private val prefix = "/api/users/"
    private val service: UserService by lazy { RetrofitServiceBuilder(context).create(UserService::class.java) }

    fun choosePlan(id: Int, payload: EmployerChoosePlanRequest) = service.choosePlan(id, payload)
}