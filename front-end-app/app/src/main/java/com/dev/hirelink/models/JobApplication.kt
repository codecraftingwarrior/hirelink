package com.dev.hirelink.models

data class JobApplication(
    val id: Int? = null,
    var state: String? = null,
    val jobOffer: JobOffer? = null,
    val applicant: ApplicationUser? = null,
    val createdAt: String? = null,
    val documents: List<Document>? = null
)