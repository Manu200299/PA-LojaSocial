package com.example.lojasocial.data.remote.model

data class DonationDto(
    var donationId: String = "",
    var donorName: String = "",
    var phoneNumber: String = "",
    var donationType: String = "",
    var description: String = "",
    var amount: Comparable<*> = 0.0
)
