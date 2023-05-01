package com.dev.hirelink.network.paymentinformation

import com.dev.hirelink.models.PaymentInformation
import com.dev.hirelink.models.PaymentInformationDTO
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface PaymentInformationService {

    @POST("payment-informations")
    fun store(@Body paymentInformation: PaymentInformationDTO): Single<PaymentInformation>
}