package com.dev.hirelink.modules.core.jobapplication

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dev.hirelink.network.jobapplication.JobApplicationRepository

class JobApplicationViewModel(
    val jobApplicationRepository: JobApplicationRepository
) : ViewModel() {

    private val _jobApplicationDone: MutableLiveData<Boolean> = MutableLiveData(false)
    val jobApplicationDone: LiveData<Boolean>
        get() = _jobApplicationDone

    fun onJobApplicationDone()  {
        _jobApplicationDone.value = true
    }

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