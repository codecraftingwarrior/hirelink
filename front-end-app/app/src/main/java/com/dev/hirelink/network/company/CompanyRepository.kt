package com.dev.hirelink.network.company

import android.content.Context
import com.dev.hirelink.components.RetrofitServiceBuilder
import com.dev.hirelink.models.Company
import com.dev.hirelink.models.WrappedPaginatedResource
import io.reactivex.Single

class CompanyRepository(private val context: Context) {
    private val service: CompanyService by lazy {
        RetrofitServiceBuilder(context).create(CompanyService::class.java)
    }

    fun findAll(pageNumber: Int = 1, name: String? = null): Single<WrappedPaginatedResource<Company>> =
        service.findAll(pageNumber, name = name)
}