package com.dev.hirelink.models

data class Notification(
    var id: Int? = null,
    var title: String? = null,
    var user: ApplicationUser? = null,
    var jobApplication: JobApplication? = null
)