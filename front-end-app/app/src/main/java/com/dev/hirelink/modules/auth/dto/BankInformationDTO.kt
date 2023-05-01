package com.dev.hirelink.modules.auth.dto

data class BankInformationDTO(
    var iban: String? = null,
    var bic: String? = null,
    val bankName: String? = null,
    var owner: String? = null
)