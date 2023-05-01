package com.dev.hirelink.models

sealed class RoleType {
    data class APIRole(val iri: String) : RoleType()
}

data class Role(val id: Int?, val code: String?, val name: String?)