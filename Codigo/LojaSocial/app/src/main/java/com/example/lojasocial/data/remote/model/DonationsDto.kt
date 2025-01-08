package com.example.lojasocial.data.remote.model

import com.example.lojasocial.domain.model.Donation
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class DonationDto(
    var donationId: String = "",
    val donorName: String = "",
    val phoneNumber: String = "",
    val donationType: String = "",
    val description: String? = "",
    val amount: Double = 0.0
){
    fun toDonation(): Donation {
        return Donation(
            donationId = donationId,
            donorName = donorName,
            phoneNumber = phoneNumber,
            donationType = donationType,
            description = description,
            amount = amount
        )
    }

}