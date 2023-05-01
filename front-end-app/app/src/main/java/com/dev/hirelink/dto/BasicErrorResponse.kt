package com.dev.hirelink.dto

data class BasicErrorResponse(
    val title: String? = null,
    val description: String? = null,
    val violations: List<ConstraintViolation>? = null,
    val message: String? = null
)