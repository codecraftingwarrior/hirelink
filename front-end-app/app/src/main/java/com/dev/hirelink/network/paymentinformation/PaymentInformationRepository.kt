package com.dev.hirelink.network.paymentinformation

import android.content.Context
import com.dev.hirelink.components.RetrofitServiceBuilder
import com.dev.hirelink.dto.PaymentInformationDTO

class PaymentInformationRepository(private val context: Context) {
    private val service: PaymentInformationService by lazy {
        RetrofitServiceBuilder(context).create(
            PaymentInformationService::class.java
        )
    }

    fun store(paymentInformation: PaymentInformationDTO) = service.store(paymentInformation)
}