package com.dev.hirelink.dto

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("token") val token: String? = null
)

data class LoginErrorResponse(
    @SerializedName("code") val code: String?,
    @SerializedName("message") val message: String
)