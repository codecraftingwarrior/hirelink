package com.dev.hirelink.models

import com.google.gson.annotations.SerializedName

data class ApplicationUser(
    val id: Int? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val nationality: String? = null,
    val birthDate: String? = null,
    val phoneNumber: String? = null,
    val address: String? = null,
    val picUrl: String? = null,
    val token: String? = null,
    val email: String? = null,
    val plainPassword: String? = null,
    val company: Company? = null,
    @SerializedName("appRole") val role: Role? = null,
    val plan: Plan? = null
    //TODO: continue for other fields
)

data class ApplicationUserRequest(
    val id: Int? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val nationality: String? = null,
    val birthDate: String? = null,
    val phoneNumber: String? = null,
    val address: String? = null,
    val picUrl: String? = null,
    val token: String? = null,
    val email: String? = null,
    val plainPassword: String? = null,
    val company: Company? = null,
    @SerializedName("appRole") val role: String? = null
)

data class BasicErrorResponse(
    val title: String? = null,
    val description: String? = null,
    val violations: List<ConstraintViolation>? = null,
    val message: String? = null
)

data class ConstraintViolation(
    val propertyPath: String? = null,
    val message: String? = null,
    val code: String? = null
)