package com.dev.hirelink.network.bankinformation

import android.content.Context
import com.dev.hirelink.components.RetrofitServiceBuilder
import com.dev.hirelink.models.BankInformation
import com.dev.hirelink.dto.BankInformationDTO

class BankInformationRepository(private val context: Context) {
    private val service: BankInformationService by lazy {
        RetrofitServiceBuilder(context).create(BankInformationService::class.java)
    }

    fun store(bankInformation: BankInformationDTO) = service.store(bankInformation)
}