package com.dev.hirelink.data

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
}