package com.example.lojasocial.data.repository

import com.example.lojasocial.data.remote.api.FirebaseApi
import com.example.lojasocial.data.remote.model.DonationDto
import com.example.lojasocial.domain.model.Beneficiary
import com.example.lojasocial.domain.model.Donation
import com.example.lojasocial.domain.repository.DonationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DonationRepositoryImpl(
    private val firebaseApi: FirebaseApi
) : DonationRepository {

    override suspend fun addDonation(donation: Donation): Result<Unit> {
        return try {
            val donationDto = donation.toDonationDto()
            firebaseApi.addDonation(donationDto)  // Assuming this returns Unit on success.
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)  // You could handle more specific exceptions here.
        }
    }
    // Funcao para extrair donations da firebase
    override suspend fun getDonations(): Flow<List<Donation>> {
        return firebaseApi.getDonations().map { dtoList ->
            dtoList.map { it.toDonation() }
        }
    }
    override suspend fun updateDonation(donation: Donation): Result<Unit> {
        return try {
            val donationDto = donation.toDonationDto()
            firebaseApi.updateDonation(donationDto)  // Assuming this returns Unit on success.
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)  // You could handle more specific exceptions here.
        }
    }
    override suspend fun deleteDonation(donationId: String): Result<Unit> {
        return try {
            firebaseApi.deleteDonation(donationId)  // Assuming this returns Unit on success.
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)  // You could handle more specific exceptions here.
        }
    }

}
