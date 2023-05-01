package com.dev.hirelink.network.bankinformation

import com.dev.hirelink.models.BankInformation
import com.dev.hirelink.dto.BankInformationDTO
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface BankInformationService {
    @POST("bank-informations")
    fun store(@Body payload: BankInformationDTO): Single<BankInformation>
}