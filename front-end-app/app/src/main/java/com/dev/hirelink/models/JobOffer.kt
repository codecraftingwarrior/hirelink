package com.dev.hirelink.models

data class JobOffer(
    val id: Int,
    val title: String,
    val description: String,
    val salary: Float,
    val fromDate: String,
    val toDate: String,
    val address: String,
    val lat: Float,
    val lng: Float,

    val companyName: String
)