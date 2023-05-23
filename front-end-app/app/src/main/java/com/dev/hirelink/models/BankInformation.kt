package com.dev.hirelink.models

import com.dev.hirelink.components.Globals.BANK_INFORMATIONS_RESOURCE_PREFIX

data class BankInformation(
    val id: Int? = null,
    val iban: String? = null,
    val bic: String? = null,
    val bankName: String? = null,
    val owner: ApplicationUser? = null
) {
    fun toIRI() = BANK_INFORMATIONS_RESOURCE_PREFIX + id
}