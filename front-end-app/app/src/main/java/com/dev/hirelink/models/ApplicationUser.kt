package com.dev.hirelink.models

import com.dev.hirelink.components.Globals.APPLICATION_USER_RESOURCE_PREFIX
import com.google.gson.annotations.SerializedName

data class ApplicationUser(
    val id: Int? = null,
    val registrationState: String? = null,
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
): IRIConvertible {
    override fun toIRI() = APPLICATION_USER_RESOURCE_PREFIX + id

    fun fullname() = "$firstName $lastName"
}
