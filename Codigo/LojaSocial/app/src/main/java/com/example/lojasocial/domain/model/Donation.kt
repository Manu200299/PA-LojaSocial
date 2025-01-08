package com.example.lojasocial.domain.model

import com.example.lojasocial.data.remote.model.DonationDto

data class Donation(
    val donationId: String = "",
    val donorName: String = "",
    val phoneNumber: String = "",
    val donationType: String = "",
    val description: String? = "",
    val amount: Double = 0.0
){
    fun toDonationDto(): DonationDto {
        return DonationDto(
            donationId = donationId,
            donorName = donorName,
            phoneNumber = phoneNumber,
            donationType = donationType,
            description = description,
            amount = amount
        )
    }

}
