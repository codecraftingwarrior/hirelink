package com.dev.hirelink.network.profession

import android.content.Context
import com.dev.hirelink.components.RetrofitServiceBuilder
import com.dev.hirelink.models.PaginatedResourceWrapper
import com.dev.hirelink.models.Profession
import io.reactivex.Single

class ProfessionRepository(private val context: Context) {
    private val service: ProfessionService by lazy {
        RetrofitServiceBuilder(context).create(ProfessionService::class.java)
    }

    fun findAll(pageNumber: Int = 1, name: String? = null): Single<PaginatedResourceWrapper<Profession>> =
        service.findAll(pageNumber, name = name)
}