package com.dev.hirelink.models

data class JobApplication(
    val id: Int? = null,
    val state: String? = null,
    val jobOffer: JobOffer? = null,
    val applicant: ApplicationUser? = null
)