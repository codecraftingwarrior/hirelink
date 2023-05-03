package com.dev.hirelink.models

data class JobOffer(
    val id: Int? = null,
    val title: String? = null,
    val description: String? = null,
    val maxSalary: Float? = null,
    val minSalary: Float? = null,
    val fromDate: String? = null,
    val toDate: String? = null,
    val address: String? = null,
    val lat: Float? = null,
    val lng: Float? = null,
    val owner: ApplicationUser? = null,
    val companyName: String? = null,
    val createdAt: String? = null
)