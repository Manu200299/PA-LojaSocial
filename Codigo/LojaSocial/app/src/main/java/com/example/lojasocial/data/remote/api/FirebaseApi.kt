package com.example.lojasocial.data.remote.api

import com.example.lojasocial.data.remote.model.BeneficiaryDto
import com.example.lojasocial.data.remote.model.VolunteerDto
import com.example.lojasocial.data.remote.model.VolunteerLoginDto
import com.example.lojasocial.domain.model.VolunteerLogin
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
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


    // Funcao para registar voluntarios na firebase
    suspend fun registerVolunteer(volunteerDto: VolunteerDto): Result<String> {
        return try {
            val existingVolunteer = volunteersRef.orderByChild("telefone").equalTo(volunteerDto.telefone).get().await()
            if (existingVolunteer.exists()) {
                return Result.failure(Exception("A volunteer with this phone number already exists"))
            }

            val newVolunteerId = volunteersRef.push().key ?: return Result.failure(Exception("Failed to generate new volunteer ID"))

            volunteerDto.volunteerId = newVolunteerId // Set the generated ID
            volunteersRef.child(newVolunteerId).setValue(volunteerDto).await()

            Result.success(newVolunteerId)
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
}
