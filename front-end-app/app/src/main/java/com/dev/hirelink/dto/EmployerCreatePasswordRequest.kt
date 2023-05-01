package com.dev.hirelink.dto

data class EmployerCreatePasswordRequest(
    var nationalUniqueNumber: String? = null,
    var email: String? = null,
    var plainPassword: String? = null
)