package com.dev.hirelink.network.user

import com.dev.hirelink.models.ApplicationUser
import com.dev.hirelink.modules.auth.dto.EmployerChoosePlanRequest
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserService {

    @PUT("users/{id}")
    fun choosePlan(
        @Path("id") id: Int,
        @Body payload: EmployerChoosePlanRequest
    ): Single<ApplicationUser>
}