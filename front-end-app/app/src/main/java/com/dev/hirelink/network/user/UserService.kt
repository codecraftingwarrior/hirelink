package com.dev.hirelink.network.user

import com.dev.hirelink.models.ApplicationUser
import com.dev.hirelink.dto.EmployerChoosePlanRequest
import com.dev.hirelink.models.WrappedPaginatedResource
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {

    @PUT("users/{id}")
    fun choosePlan(
        @Path("id") id: Int,
        @Body payload: EmployerChoosePlanRequest
    ): Single<ApplicationUser>

    @GET("users/registration-state")
    fun findAll(
        @Query("page") page: Int? = 1,
        @Query("role.code") code: String? = null
    ): Single<WrappedPaginatedResource<ApplicationUser>>
}