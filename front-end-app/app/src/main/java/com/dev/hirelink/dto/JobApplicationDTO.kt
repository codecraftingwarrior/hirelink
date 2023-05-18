package com.dev.hirelink.dto

import okhttp3.MultipartBody

data class JobApplicationDTO(
    var jobOffer: String? = null,
    val applicant: String? = null,
    val documents: List<MultipartBody.Part>
)