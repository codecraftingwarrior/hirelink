package com.dev.hirelink.modules.auth.dto

data class OTPVerificationRequest(
    var userEmail: String? = null,
    var userID: Int? = null,
    var otpDigit: String? = null
)