package com.dev.hirelink.models

data class PaymentInformation(
    var creditCardNumber: String? = null,
    var cvv: String? = null,
    var holderName: String? = null,
    var expDate: String? = null,
    var owner: ApplicationUser? = null
)