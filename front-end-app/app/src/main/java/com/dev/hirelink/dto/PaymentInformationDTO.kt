package com.dev.hirelink.dto

data class PaymentInformationDTO(
    var creditCardNumber: String? = null,
    var cvv: String? = null,
    var holderName: String? = null,
    var expDate: String? = null,
    var owner: String? = null
)