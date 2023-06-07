package com.dev.hirelink.dto

data class Dashboard(
    val candidatureCount: Int? = null,
    val announceCount: Int? = null,
    val mostRequestedJobs: List<FrequencyItem>? = null,
    val mostProposedJobs: List<FrequencyItem>? = null
)

data class FrequencyItem(
    val name: String? = null,
    val frequency: Int? = null
)