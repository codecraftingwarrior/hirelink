package com.dev.hirelink.dto

import com.dev.hirelink.models.JobApplication

data class WrappedJobApplication(
    val jobApplications: List<JobApplication>? = null
)