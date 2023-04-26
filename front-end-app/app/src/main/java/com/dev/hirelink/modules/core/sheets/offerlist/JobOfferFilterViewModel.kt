package com.dev.hirelink.modules.core.sheets.offerlist

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class JobOfferFilterViewModel : ViewModel() {
    private val _criteria: MutableLiveData<JobOfferFilterCriteria> by lazy {
        MutableLiveData(
            JobOfferFilterCriteria()
        )
    }
    val criteria: LiveData<JobOfferFilterCriteria>
        get() = _criteria

    fun updateCriteria(newCriteria: JobOfferFilterCriteria) {
        _criteria.value = newCriteria
    }

    data class JobOfferFilterCriteria(
        var startDate: String? = null,
        var endDate: String? = null,
        var minSalary: Float? = 0f,
        var maxSalary: Float? = 0f
    )

    class JobOfferFilterViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(JobOfferFilterViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return JobOfferFilterViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}