package com.dev.hirelink.enums

import com.dev.hirelink.R

enum class JobApplicationState(val colorResourceId: Int, val stringResourceId: Int) {
    PENDING(R.color.indigo_main, R.string.job_application_pending_state),
    IN_PROGRESS(R.color.amber_600, R.string.job_application_in_progress_state),
    ACCEPTED(R.color.state_green, R.string.job_application_accepted_state),
    REFUSED(R.color.red_A700, R.string.job_application_refused_state)
}