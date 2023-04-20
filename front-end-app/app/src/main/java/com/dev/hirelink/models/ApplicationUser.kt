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
    @SerializedName("appRole") val role: Role? = null,
    val token: String? = null,
    val email: String? = null
    //TODO: continue for other fields
)