package com.example.lojasocial.domain.model

data class Donation(
    val donationId: String = "",
    val donorName: String = "",
    val phoneNumber: String = "",
    val donationType: String = "",
    val description: String? = "",
    val amount: Comparable<*> = 0.0,
)
