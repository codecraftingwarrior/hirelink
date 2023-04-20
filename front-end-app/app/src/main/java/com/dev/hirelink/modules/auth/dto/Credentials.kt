package com.dev.hirelink.modules.auth.dto

import com.google.gson.annotations.SerializedName


data class Credentials(
    @SerializedName("username") val username: String?,
    @SerializedName("password") val password: String?
)