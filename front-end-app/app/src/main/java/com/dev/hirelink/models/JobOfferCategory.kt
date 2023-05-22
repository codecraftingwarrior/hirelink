package com.dev.hirelink.models

import com.dev.hirelink.components.Globals.JOB_OFFER_CATEGORY_RESOURCE_PREFIX

data class JobOfferCategory(
    val id: String? = null,
    val name: String? = null
) : IRIConvertible {
    override fun toIRI(): String {
        return JOB_OFFER_CATEGORY_RESOURCE_PREFIX + id
    }
}