package com.dev.hirelink.dto

import com.dev.hirelink.models.Company
import com.google.gson.annotations.SerializedName

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