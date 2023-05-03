package com.dev.hirelink.modules.core.offers.list

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dev.hirelink.modules.core.offers.list.filter.JobOfferFilterViewModel
import com.dev.hirelink.network.joboffer.JobOfferRepository

class JobOfferViewModel(
    val jobOfferRepository: JobOfferRepository
) : ViewModel() {


    class JobOfferViewModelFactory(
        private val context: Context,
        private val jobOfferRepository: JobOfferRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(JobOfferViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return JobOfferViewModel(jobOfferRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}