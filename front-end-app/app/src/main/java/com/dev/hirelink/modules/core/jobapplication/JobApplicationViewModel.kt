package com.dev.hirelink.modules.core.jobapplication

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dev.hirelink.models.JobApplication
import com.dev.hirelink.modules.core.offers.list.filter.JobOfferFilterViewModel
import com.dev.hirelink.network.jobapplication.JobApplicationRepository

class JobApplicationViewModel(
    val jobApplicationRepository: JobApplicationRepository
) : ViewModel() {

    private val _jobApplicationDone: MutableLiveData<Boolean> = MutableLiveData(false)
    val jobApplicationDone: LiveData<Boolean>
        get() = _jobApplicationDone

    private val _criteria: MutableLiveData<JobApplicationFilterCriteria> by lazy {
        MutableLiveData(
            JobApplicationFilterCriteria()
        )
    }

    val criteria: LiveData<JobApplicationFilterCriteria>
        get() = _criteria

    fun updateCriteria(newCriteria: JobApplicationFilterCriteria) {
        _criteria.value = newCriteria
    }


    fun onJobApplicationDone() {
        _jobApplicationDone.value = true
    }

    fun findById(id: Int) = jobApplicationRepository.findById(id)

    fun update(jobApplication: JobApplication) = jobApplicationRepository.update(jobApplication)

    fun findJobApplications(
        page: Int = 1,
        state: String? = null,
        searchQuery: String?
    ) = jobApplicationRepository.findAll(page, state, searchQuery)

    data class JobApplicationFilterCriteria(
        var searchQuery: String? = null
    )

    class JobApplicationViewModelFactory(
        private val context: Context,
        private val jobApplicationRepository: JobApplicationRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(JobApplicationViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return JobApplicationViewModel(jobApplicationRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}