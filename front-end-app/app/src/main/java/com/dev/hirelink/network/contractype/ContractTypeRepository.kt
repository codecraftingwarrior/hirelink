package com.dev.hirelink.network.contractype

import android.content.Context
import com.dev.hirelink.components.RetrofitServiceBuilder
import com.dev.hirelink.models.ContractType
import io.reactivex.Single
import retrofit2.Retrofit

class ContractTypeRepository(
    private val context: Context
) {
    private val service: ContractTypeService by lazy {
        RetrofitServiceBuilder(context).create(ContractTypeService::class.java)
    }

    fun findAll(): Single<List<ContractType>> = service.findAll()
}