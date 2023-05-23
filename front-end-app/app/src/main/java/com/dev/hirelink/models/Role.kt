package com.dev.hirelink.models

import com.dev.hirelink.components.Globals

data class Role(val id: Int?, val code: String?, val name: String?) {
    fun toIRI() = Globals.ROLE_RESOURCE_PREFIX + id
}