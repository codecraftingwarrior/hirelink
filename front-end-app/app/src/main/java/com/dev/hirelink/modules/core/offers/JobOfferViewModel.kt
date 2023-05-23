package com.dev.hirelink.modules.core.offers

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dev.hirelink.models.JobOffer
import com.dev.hirelink.network.auth.AuthRepository
import com.dev.hirelink.network.contractype.ContractTypeRepository
import com.dev.hirelink.network.jobcategory.JobCategoryRepository
import com.dev.hirelink.network.joboffer.JobOfferRepository
import com.dev.hirelink.network.profession.ProfessionRepository

class JobOfferViewModel(
    val jobOfferRepository: JobOfferRepository,
    val contractTypeRepository: ContractTypeRepository,
    val categoryRepository: JobCategoryRepository,
    val professionRepository: ProfessionRepository,
    val authRepository: AuthRepository
) : ViewModel() {
    private val _addedJobOffer: MutableLiveData<JobOffer> = MutableLiveData(JobOffer())

    val addedJobOffer: LiveData<JobOffer>
        get() = _addedJobOffer

    fun fetchAllContractTypes() = contractTypeRepository.findAll()

    fun fetchAllJobCategories() = categoryRepository.findAll()

    fun fetchAllProfessions() = professionRepository.findAllNotPaginated()

    fun createJobOffer(jobOffer: JobOffer) = jobOfferRepository.create(jobOffer)

    fun findJobApplicationsByJobOfferId(jobOffer: JobOffer) = jobOfferRepository.findJobApplicationsByJobOfferId(jobOffer.id!!)

    fun onAddedJobOffer(jobOffer: JobOffer)  {
        _addedJobOffer.value = jobOffer
    }


    class JobOfferViewModelFactory(
        private val context: Context,
        private val jobOfferRepository: JobOfferRepository,
        private val contractTypeRepository: ContractTypeRepository,
        private val jobCategoryRepository: JobCategoryRepository,
        private val professionRepository: ProfessionRepository,
        private val authRepository: AuthRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(JobOfferViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return JobOfferViewModel(
                    jobOfferRepository,
                    contractTypeRepository,
                    jobCategoryRepository,
                    professionRepository,
                    authRepository
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}