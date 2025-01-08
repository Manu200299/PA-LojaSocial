package com.example.lojasocial.data.repository

import com.example.lojasocial.data.remote.api.FirebaseApi
import com.example.lojasocial.data.remote.model.DonationDto
import com.example.lojasocial.domain.model.Donation
import com.example.lojasocial.domain.repository.DonationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class DonationRepositoryImpl(
    private val firebaseApi: FirebaseApi
) : DonationRepository {

    /**
     * Fetch all donations from Firebase.
     * @return A Flow of list of DonationDto objects.
     */
    override suspend fun getDonations(): Flow<List<Donation>> {
        return flow {
            val firebaseDatabase = FirebaseDatabase.getInstance()
            val donationsReference = firebaseDatabase.getReference("donations")

            val snapshot = donationsReference.get().await()  // Use 'await()' to wait for the result asynchronously
            if (snapshot.exists()) {
                val donationsMap = snapshot.value as? Map<String, Any>
                val donationDtos = donationsMap?.mapNotNull { entry ->
                    val donationData = entry.value as? Map<String, Any>
                    donationData?.let {
                        DonationDto(
                            donationId = entry.key,
                            donorName = it["donorName"] as? String ?: "",
                            phoneNumber = it["phoneNumber"] as? String ?: "",
                            donationType = it["donationType"] as? String ?: "",
                            description = it["description"] as? String ?: "",
                            amount = it["amount"] as? Long ?: 0L
                        )
                    }
                } ?: emptyList()

                // Convert DonationDto to Donation and emit the list
                val donations = donationDtos.map { it.toDomain() }
                emit(donations)  // Ensure donations are emitted here
            }
        }
    }

    /**
     * Add a new donation to Firebase.
     * @param donation The donation object to add.
     * @return Result of the operation.
     */
    override suspend fun addDonation(donation: Donation): Result<Unit> {
        return try {
            val donationDto = donation.toDto()
            firebaseApi.addDonation(donationDto)  // Assuming this returns Unit on success.
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)  // You could handle more specific exceptions here.
        }
    }

    /**
     * Update an existing donation in Firebase.
     * @param donation The donation object with updated details.
     * @return Result of the operation.
     */
    override suspend fun updateDonation(donation: Donation): Result<Unit> {
        return try {
            val donationDto = donation.toDto()
            firebaseApi.updateDonation(donationDto)  // Assuming this returns Unit on success.
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)  // You could handle more specific exceptions here.
        }
    }

    /**
     * Delete a donation from Firebase.
     * @param donationId The ID of the donation to delete.
     * @return Result of the operation.
     */
    override suspend fun deleteDonation(donationId: String): Result<Unit> {
        return try {
            firebaseApi.deleteDonation(donationId)  // Assuming this returns Unit on success.
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)  // You could handle more specific exceptions here.
        }
    }

    // Convert from DTO to Domain model
    private fun DonationDto.toDomain(): Donation {
        return Donation(
            donationId = this.donationId,
            donorName = this.donorName,
            phoneNumber = this.phoneNumber,
            donationType = this.donationType,
            description = this.description,
            amount = this.amount,
        )
    }

    // Convert from Domain model to DTO
    private fun Donation.toDto(): DonationDto {
        return DonationDto(
            donationId = this.donationId,
            donorName = this.donorName,
            phoneNumber = this.phoneNumber,
            donationType = this.donationType,
            description = this.description ?: "",  // Ensure description is not null
            amount = this.amount,
        )
    }
}
