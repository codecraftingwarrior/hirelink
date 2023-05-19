package com.dev.hirelink.models

import com.dev.hirelink.components.Globals.JOB_OFFER_RESOURCE_PREFIX

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
    val createdAt: String? = null,
    val contractType: ContractType? = null,
    val category: JobOfferCategory? = null,
    val applicantCount: Int? = 0,
    val profession: Profession? = null
) {
    fun toIRI() = JOB_OFFER_RESOURCE_PREFIX + id
}