package com.dev.hirelink.models

data class Company(
     var id: Int? = null,
     var name: String? = null,
     var nationalUniqueNumber: String? = null,
     var mailAddress: String? = null,
     var address: String? = null,
     var city: String? = null,
     var country: String? = null,
     var lat: Double? = null,
     var lng: Double? = null
)