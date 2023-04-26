package com.dev.hirelink.network.role

import android.content.Context
import com.dev.hirelink.components.RetrofitServiceBuilder
import com.dev.hirelink.models.Role
import com.dev.hirelink.models.RoleType

class RoleRepository(private val context: Context) {
    private val prefix = "/api/roles/"
    private val service: RoleService by lazy { RetrofitServiceBuilder(context).create(RoleService::class.java) }

    fun toIRI(role: Role): String = RoleType.APIRole(prefix + role.id).iri
}