package com.dev.hirelink.models

data class Plan(
    val id: Int,
    val name: String,
    val price: Float,
    val conditions: String,
    val unsubscriptionConditions: String
)