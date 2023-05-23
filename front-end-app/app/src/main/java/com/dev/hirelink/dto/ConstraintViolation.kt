package com.dev.hirelink.dto

data class ConstraintViolation(
    val propertyPath: String? = null,
    val message: String? = null,
    val code: String? = null
)