package com.dev.hirelink.modules.core.offers.list.filter

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dev.hirelink.models.Company
import com.dev.hirelink.network.company.CompanyRepository

class JobOfferFilterViewModel(
    val companyRepository: CompanyRepository
) : ViewModel() {
    private val _chosenCompanies: MutableLiveData<MutableList<Company?>?> by lazy {
        MutableLiveData(mutableListOf())
    }
    val chosenCompanies: LiveData<MutableList<Company?>?>
        get() = _chosenCompanies

    private val _criteria: MutableLiveData<JobOfferFilterCriteria> by lazy {
        MutableLiveData(
            JobOfferFilterCriteria()
        )
    }

    val criteria: LiveData<JobOfferFilterCriteria>
        get() = _criteria

    fun updateChosenCompanies(companies: MutableList<Company?>) {
        _chosenCompanies.value = companies
    }

    fun updateCriteria(newCriteria: JobOfferFilterCriteria) {
        _criteria.value = newCriteria
    }

    fun getCriteria(): JobOfferFilterCriteria {
        return _criteria.value!!;
    }

    data class JobOfferFilterCriteria(
        var fromDate: String? = null,
        var toDate: String? = null,
        var minSalary: Float? = null,
        var maxSalary: Float? = null,
        var latitude: Double? = null,
        var longitude: Double? = null,
        var maxDistance: Double? = null,
        var jobTitle: String? = null,
        var chosenCompanyIds: List<Int>? = null
    )

    class JobOfferFilterViewModelFactory(
        private val context: Context,
        private val companyRepository: CompanyRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(JobOfferFilterViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return JobOfferFilterViewModel(companyRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}