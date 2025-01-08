package com.example.lojasocial.domain.repository

import com.example.lojasocial.domain.model.Donation
import kotlinx.coroutines.flow.Flow

interface DonationRepository {
    suspend fun addDonation(donation: Donation): Result<Unit>
    suspend fun getDonations(): Flow<List<Donation>>
    suspend fun updateDonation(donation: Donation): Result<Unit>
    suspend fun deleteDonation(donationId: String): Result<Unit>
}


