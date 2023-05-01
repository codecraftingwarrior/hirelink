package com.dev.hirelink.models

import com.dev.hirelink.components.Globals.PLAN_RESOURCE_PREFIX

data class Plan(
    val id: Int? = null,
    val name: String? = null,
    val price: Float? = null,
    val conditions: String? = null,
    val unsubscriptionConditions: String? = null
) {
    fun toIRI() = PLAN_RESOURCE_PREFIX + id
}