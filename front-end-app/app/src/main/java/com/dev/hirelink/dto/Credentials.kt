package com.dev.hirelink.dto

import com.google.gson.annotations.SerializedName


data class Credentials(
    @SerializedName("username") val username: String?,
    @SerializedName("password") val password: String?
)