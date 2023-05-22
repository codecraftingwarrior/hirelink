package com.dev.hirelink.data

import com.dev.hirelink.enums.JobApplicationState
import com.dev.hirelink.models.*

class MockDataSource {

    fun loadJobApplications(): List<JobApplication> {
        return listOf(
            JobApplication(
                id = 1,
                jobOffer = JobOffer(
                    id = 1,
                    title = "Cleaning Agent",
                    description = "Nunc ut viverra ac massa agam tincidunt tritani. Vero audire sumo curae corrumpit possim etiam tritani.",
                    maxSalary = 45000.0f,
                    fromDate = "10/10/2023",
                    toDate = "10/10/2024",
                    address = "6648 Shantae Brooks",
                    lat = 3003.0,
                    lng = 300493.0,
                    companyName = "McDonald's"
                ),
                applicant = ApplicationUser(id = 1),
                state = JobApplicationState.REFUSED.name
            ),
            JobApplication(
                id = 2,
                jobOffer = JobOffer(
                    id = 2,
                    title = "Machine Learning Engineer",
                    description = "Nunc ut viverra ac massa agam tincidunt tritani. Vero audire sumo curae corrumpit possim etiam tritani.",
                    maxSalary = 85000.0f,
                    fromDate = "10/10/2023",
                    toDate = "10/10/2024",
                    address = "6648 Shantae Brooks",
                    lat = 3003.0,
                    lng = 300493.0,
                    companyName = "Apple"
                ),
                applicant = ApplicationUser(id = 1),
                state = JobApplicationState.ACCEPTED.name
            ),
            JobApplication(
                id = 1,
                jobOffer = JobOffer(
                    id = 3,
                    title = "Software Engineer",
                    description = "Nunc ut viverra ac massa agam tincidunt tritani. Vero audire sumo curae corrumpit possim etiam tritani.",
                    maxSalary = 65000.0f,
                    fromDate = "10/10/2023",
                    toDate = "10/10/2024",
                    address = "6648 Shantae Brooks",
                    lat = 3003.0,
                    lng = 300493.0,
                    companyName = "Amazon"
                ),
                applicant = ApplicationUser(id = 1),
                state = JobApplicationState.PENDING.name
            ),
            JobApplication(
                id = 1,
                jobOffer = JobOffer(
                    id = 5,
                    title = "UI/UX Designer",
                    description = "Nunc ut viverra ac massa agam tincidunt tritani. Vero audire sumo curae corrumpit possim etiam tritani.",
                    maxSalary = 105000.0f,
                    fromDate = "10/10/2023",
                    toDate = "10/10/2024",
                    address = "6648 Shantae Brooks",
                    lat = 3003.0,
                    lng = 300493.0,
                    companyName = "Jira"
                ),
                applicant = ApplicationUser(id = 1),
                state = JobApplicationState.IN_PROGRESS.name
            ),
            JobApplication(
                id = 1,
                jobOffer = JobOffer(
                    id = 1,
                    title = "Cleaning Agent",
                    description = "Nunc ut viverra ac massa agam tincidunt tritani. Vero audire sumo curae corrumpit possim etiam tritani.",
                    maxSalary = 45000.0f,
                    fromDate = "10/10/2023",
                    toDate = "10/10/2024",
                    address = "6648 Shantae Brooks",
                    lat = 3003.0,
                    lng = 300493.0,
                    companyName = "McDonald's"
                ),
                applicant = ApplicationUser(id = 1),
                state = JobApplicationState.PENDING.name
            ),
        )
    }

}