package com.dev.hirelink.data

import com.dev.hirelink.enums.JobApplicationState
import com.dev.hirelink.models.ApplicationUser
import com.dev.hirelink.models.JobApplication
import com.dev.hirelink.models.JobOffer
import com.dev.hirelink.models.Plan

class MockDataSource {
    fun loadPlans(): List<Plan> {
        return listOf<Plan>(
            Plan(
                id = 1,
                name = "Start",
                price = 10.0f,
                conditions = "Docendi eruditi explicari ubique nobis quem integer dictumst nullam in. Has quaeque varius utroque habitasse lacus ancillae.",
                unsubscriptionConditions = "Docendi eruditi explicari ubique nobis quem integer dictumst nullam in. Has quaeque varius utroque habitasse lacus ancillae."
            ),
            Plan(
                id = 2,
                name = "Basic",
                price = 200.0f,
                conditions = "Docendi eruditi explicari ubique nobis quem integer dictumst nullam in. Has quaeque varius utroque habitasse lacus ancillae.",
                unsubscriptionConditions = "Docendi eruditi explicari ubique nobis quem integer dictumst nullam in. Has quaeque varius utroque habitasse lacus ancillae."
            ),
            Plan(
                id = 3,
                name = "Standard",
                price = 400.0f,
                conditions = "Docendi eruditi explicari ubique nobis quem integer dictumst nullam in. Has quaeque varius utroque habitasse lacus ancillae.",
                unsubscriptionConditions = "Docendi eruditi explicari ubique nobis quem integer dictumst nullam in. Has quaeque varius utroque habitasse lacus ancillae."
            ),
            Plan(
                id = 4,
                name = "Advanced",
                price = 600.0f,
                conditions = "Docendi eruditi explicari ubique nobis quem integer dictumst nullam in. Has quaeque varius utroque habitasse lacus ancillae.",
                unsubscriptionConditions = "Docendi eruditi explicari ubique nobis quem integer dictumst nullam in. Has quaeque varius utroque habitasse lacus ancillae."
            ),
            Plan(
                id = 5,
                name = "Premium",
                price = 1000.0f,
                conditions = "Docendi eruditi explicari ubique nobis quem integer dictumst nullam in. Has quaeque varius utroque habitasse lacus ancillae.",
                unsubscriptionConditions = "Docendi eruditi explicari ubique nobis quem integer dictumst nullam in. Has quaeque varius utroque habitasse lacus ancillae."
            ),
            Plan(
                id = 6,
                name = "Ultimate",
                price = 1500.0f,
                conditions = "Docendi eruditi explicari ubique nobis quem integer dictumst nullam in. Has quaeque varius utroque habitasse lacus ancillae.",
                unsubscriptionConditions = "Docendi eruditi explicari ubique nobis quem integer dictumst nullam in. Has quaeque varius utroque habitasse lacus ancillae."
            )
        )
    }

    fun loadJobOffers(): List<JobOffer> {
        return listOf(
            JobOffer(
                id = 1,
                title = "Cleaning Agent",
                description = "Nunc ut viverra ac massa agam tincidunt tritani. Vero audire sumo curae corrumpit possim etiam tritani.",
                maxSalary = 45000.0f,
                fromDate = "10/10/2023",
                toDate = "10/10/2024",
                address = "6648 Shantae Brooks",
                lat = 3003.0f,
                lng = 300493.0f,
                companyName = "McDonald's"
            ),
            JobOffer(
                id = 2,
                title = "Machine Learning Engineer",
                description = "Nunc ut viverra ac massa agam tincidunt tritani. Vero audire sumo curae corrumpit possim etiam tritani.",
                maxSalary = 85000.0f,
                fromDate = "10/10/2023",
                toDate = "10/10/2024",
                address = "6648 Shantae Brooks",
                lat = 3003.0f,
                lng = 300493.0f,
                companyName = "Apple"
            ),
            JobOffer(
                id = 3,
                title = "Software Engineer",
                description = "Nunc ut viverra ac massa agam tincidunt tritani. Vero audire sumo curae corrumpit possim etiam tritani.",
                maxSalary = 65000.0f,
                fromDate = "10/10/2023",
                toDate = "10/10/2024",
                address = "6648 Shantae Brooks",
                lat = 3003.0f,
                lng = 300493.0f,
                companyName = "Amazon"
            ),
            JobOffer(
                id = 4,
                title = "Cleaning Agent",
                description = "Nunc ut viverra ac massa agam tincidunt tritani. Vero audire sumo curae corrumpit possim etiam tritani.",
                maxSalary = 45000.0f,
                fromDate = "10/10/2023",
                toDate = "10/10/2024",
                address = "6648 Shantae Brooks",
                lat = 3003.0f,
                lng = 300493.0f,
                companyName = "Ali BaBa"
            ),
            JobOffer(
                id = 5,
                title = "UI/UX Designer",
                description = "Nunc ut viverra ac massa agam tincidunt tritani. Vero audire sumo curae corrumpit possim etiam tritani.",
                maxSalary = 105000.0f,
                fromDate = "10/10/2023",
                toDate = "10/10/2024",
                address = "6648 Shantae Brooks",
                lat = 3003.0f,
                lng = 300493.0f,
                companyName = "Jira"
            ),
        )
    }

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
                    lat = 3003.0f,
                    lng = 300493.0f,
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
                    lat = 3003.0f,
                    lng = 300493.0f,
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
                    lat = 3003.0f,
                    lng = 300493.0f,
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
                    lat = 3003.0f,
                    lng = 300493.0f,
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
                    lat = 3003.0f,
                    lng = 300493.0f,
                    companyName = "McDonald's"
                ),
                applicant = ApplicationUser(id = 1),
                state = JobApplicationState.PENDING.name
            ),
        )
    }
}