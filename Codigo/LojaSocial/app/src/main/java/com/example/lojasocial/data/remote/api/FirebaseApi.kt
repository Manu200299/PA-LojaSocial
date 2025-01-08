package com.example.lojasocial.data.remote.api

import android.util.Log
import com.example.lojasocial.data.remote.model.BeneficiaryDto
import com.example.lojasocial.data.remote.model.DonationDto
import com.example.lojasocial.data.remote.model.StockItemDto
import com.example.lojasocial.data.remote.model.VisitDto
import com.example.lojasocial.data.remote.model.VolunteerDto
import com.example.lojasocial.data.remote.model.VolunteerLoginDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

// Calls para a API da Firebase
class FirebaseApi(
    private val auth: FirebaseAuth,
    val database: FirebaseDatabase){

    // Nome das tabelas no firebase
    private val beneficiariesRef = database.getReference("Beneficiarios")
    private val volunteersRef = database.getReference("Voluntarios")
    private val stockRef = database.getReference("Stock")
    private val donationsRef = database.getReference("Donations") // mudar para portugues
    private val visitsRef = database.getReference("Visitas")



    // Funcao para adicionar beneficiarios
    suspend fun addBeneficiary(beneficiaryDto: BeneficiaryDto): Result<Unit> {
        return try {
            beneficiariesRef.child(beneficiaryDto.id).setValue(beneficiaryDto).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Funcao para extrair beneficiarios da firebase
    suspend fun getBeneficiaries(): Flow<List<BeneficiaryDto>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val beneficiaries = snapshot.children.mapNotNull { it.getValue(BeneficiaryDto::class.java) }
                trySend(beneficiaries)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        beneficiariesRef.addValueEventListener(listener)

        awaitClose {
            beneficiariesRef.removeEventListener(listener)
        }
    }

    // Funcao para extrair um beneficiario atraves do seu ID
    suspend fun getBeneficiaryById(beneficiaryId: String): BeneficiaryDto?{
        return try{
            val snapshot = beneficiariesRef.child(beneficiaryId).get().await()
            snapshot.getValue(BeneficiaryDto::class.java)
        } catch (e: Exception){
            Log.e("FirebaseApi", "Error getting beneficairy by ID: ${e.message}")
            null
        }
    }


    // Funcao para registar voluntarios na firebase
    suspend fun registerVolunteer(volunteerDto: VolunteerDto): Result<VolunteerDto> {
        return try {
            val existingVolunteer = volunteersRef.orderByChild("telefone").equalTo(volunteerDto.telefone).get().await()
            if (existingVolunteer.exists()) {
                return Result.failure(Exception("A volunteer with this phone number already exists"))
            }

            val newVolunteerId = volunteersRef.push().key ?: return Result.failure(Exception("Failed to generate new volunteer ID"))

            volunteerDto.volunteerId = newVolunteerId // Set the generated ID
            volunteersRef.child(newVolunteerId).setValue(volunteerDto).await()

            Result.success(volunteerDto)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Funcao para logar voluntarios
    suspend fun loginVolunteer(volunteerLoginDto: VolunteerLoginDto): Result<VolunteerDto>{
        return try{
            val query = volunteersRef.orderByChild("telefone").equalTo(volunteerLoginDto.telefone)
            val snapshot = query.get().await()

            if (snapshot.exists()){
                val volunteerData = snapshot.children.first().getValue(VolunteerDto::class.java)
                if (volunteerData != null && volunteerData.password == volunteerLoginDto.password){
                    Result.success(volunteerData)
                } else {
                    Result.failure(Exception("Invalid password"))
                }
            }else {
                Result.failure(Exception("Volunteer not found"))
            }
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    // CREATE: adicionar um item ao stock
    suspend fun addStockItem(stockItemDto: StockItemDto): Result<Unit> {
        return try {
            val newItemId = stockRef.push().key
                ?: return Result.failure(Exception("Falha ao gerar ID para o item de stock."))

            stockItemDto.itemId = newItemId
            stockRef.child(newItemId).setValue(stockItemDto).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // READ: obter todos os itens em stock
    // (usa Flow, tal como fazes com beneficiaries, volunteers, etc.)
    suspend fun getStockItems(): kotlinx.coroutines.flow.Flow<List<StockItemDto>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = snapshot.children.mapNotNull {
                    it.getValue(StockItemDto::class.java)
                }
                trySend(items)
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        stockRef.addValueEventListener(listener)
        awaitClose {
            stockRef.removeEventListener(listener)
        }
    }

    // UPDATE: actualizar um item
    suspend fun updateStockItem(stockItemDto: StockItemDto): Result<Unit> {
        return try {
            if (stockItemDto.itemId.isBlank()) {
                return Result.failure(Exception("ID do item vazio."))
            }
            stockRef.child(stockItemDto.itemId).setValue(stockItemDto).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // DELETE: remover item
    suspend fun deleteStockItem(itemId: String): Result<Unit> {
        return try {
            stockRef.child(itemId).removeValue().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    // CREATE: Add a new donation
    suspend fun addDonation(donationDto: DonationDto): Result<Unit> {
        return try {
            val newDonationId = donationsRef.push().key
                ?: return Result.failure(Exception("Failed to generate a donation ID."))

            donationDto.donationId = newDonationId
            donationsRef.child(newDonationId).setValue(donationDto).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // READ: Get all donations
    suspend fun getDonations(): Flow<List<DonationDto>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val donations = snapshot.children.mapNotNull { it.getValue(DonationDto::class.java) }
                trySend(donations)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        donationsRef.addValueEventListener(listener)
        awaitClose {
            donationsRef.removeEventListener(listener)
        }
    }

    // READ: Get a donation by ID
    suspend fun getDonationById(donationId: String): DonationDto? {
        return try {
            val snapshot = donationsRef.child(donationId).get().await()
            snapshot.getValue(DonationDto::class.java)
        } catch (e: Exception) {
            Log.e("FirebaseApi", "Error getting donation by ID: ${e.message}")
            null
        }
    }

    // UPDATE: Update a donation
    suspend fun updateDonation(donationDto: DonationDto): Result<Unit> {
        return try {
            if (donationDto.donationId.isBlank()) {
                return Result.failure(Exception("Donation ID is empty."))
            }
            donationsRef.child(donationDto.donationId).setValue(donationDto).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // DELETE: Delete a donation by ID
    suspend fun deleteDonation(donationId: String): Result<Unit> {
        return try {
            donationsRef.child(donationId).removeValue().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Funcao para criar visitas
    suspend fun createVisit(visitDto: VisitDto): Result<VisitDto>{
        return try {
            val newVisitRef = visitsRef.push()
            val visitWithId = visitDto.copy(id = newVisitRef.key ?: "")
            newVisitRef.setValue(visitWithId).await()
            Result.success(visitWithId)
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    // Funcao para atualizar visita
    suspend fun updateVisit(visitDto: VisitDto): Result<Unit>{
        return try {
            visitsRef.child(visitDto.id).setValue(visitDto).await()
            Result.success(Unit)
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    // Funcao para dar fetch as visitas pelo id do beneficiario
    suspend fun getVisitsByBeneficiaryId(beneficiaryId: String): Flow<List<VisitDto>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val visits = snapshot.children.mapNotNull { it.getValue(VisitDto::class.java) }
                trySend(visits)
            } override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        visitsRef.orderByChild("beneficiaryId").equalTo(beneficiaryId).addValueEventListener(listener)
        awaitClose { visitsRef.removeEventListener(listener)}
    }

    // Funcao para ver se o beneficiario tem uma visita ativa
    suspend fun getActiveVisitForBeneficiary(beneficiaryId: String): VisitDto? {
        return try {
            val snapshot = visitsRef
                .orderByChild("beneficiaryId")
                .equalTo(beneficiaryId)
                .get()
                .await()

            snapshot.children
                .mapNotNull { it.getValue(VisitDto::class.java) }
                .firstOrNull { !it.isFinished && it.beneficiaryId == beneficiaryId }
        } catch (e: Exception){
            null
        }
    }

    // Funcao para finalizar visita (checkout)
    suspend fun finalizeVisit(visitId: String): Result<Unit>{
        return try{
            visitsRef.child(visitId).child("isFinished").setValue(true).await()
            Result.success(Unit)
        } catch (e:Exception){
            Result.failure(e)
        }
    }
}
A