package com.dev.hirelink.network.role

import com.dev.hirelink.models.Role
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface RoleService {

    @Headers("Accept: application/json", "Content-type: application/json")
    @GET("roles")
    fun findAll(
        @Query("page") pageNumber: Int = 1,
        @Query("code[]") code: List<String>? = null
    ): Single<List<Role>>
}