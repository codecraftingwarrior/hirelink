package com.dev.hirelink.network.role

import com.dev.hirelink.models.Role
import io.reactivex.Single
import retrofit2.http.GET

interface RoleService {

    @GET("roles")
    fun findAll(): Single<List<Role>>
}