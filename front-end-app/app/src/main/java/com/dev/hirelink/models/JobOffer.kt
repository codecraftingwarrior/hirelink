package com.dev.hirelink.models

import com.dev.hirelink.components.Globals.JOB_OFFER_RESOURCE_PREFIX
import com.dev.hirelink.serializer.ToIriSerializer
import com.google.gson.annotations.JsonAdapter

data class JobOffer(
    var id: Int? = null,
    var title: String? = null,
    var description: String? = null,
    var maxSalary: Float? = null,
    var minSalary: Float? = null,
    var fromDate: String? = null,
    var toDate: String? = null,
    var address: String? = null,
    var lat: Double? = null,
    var lng: Double? = null,
    var city: String? = null,
    var country: String? = null,
    @field:JsonAdapter(ToIriSerializer::class)
    var owner: ApplicationUser? = null,
    var companyName: String? = null,
    var createdAt: String? = null,
    @field:JsonAdapter(ToIriSerializer::class)
    var contractType: ContractType? = null,
    @field:JsonAdapter(ToIriSerializer::class)
    var category: JobOfferCategory? = null,
    var applicantCount: Int? = 0,
    @field:JsonAdapter(ToIriSerializer::class)
    var profession: Profession? = null
): IRIConvertible {
    override fun toIRI() = JOB_OFFER_RESOURCE_PREFIX + id
}