package com.dev.hirelink.network.contractype

import com.dev.hirelink.models.ContractType
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers

interface ContractTypeService {

    @Headers("Accept: application/json", "Content-type: application/json")
    @GET("contract-types")
    fun findAll(): Single<List<ContractType>>
}