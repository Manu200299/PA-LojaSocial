package com.example.lojasocial.data.remote

import com.example.lojasocial.data.model.Beneficiary
import com.example.lojasocial.data.model.Volunteer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

// Calls para a API da Firebase
class FirebaseApi(
    private val auth: FirebaseAuth,
    private val database: FirebaseDatabase){

    // Nome da table beneficiarios no firebase
    private val beneficiariesRef = database.getReference("Beneficiarios")
    private val volunteersRef = database.getReference("Voluntarios")

    suspend fun registerVolunteer(volunteer: Volunteer): Result<Unit> {
        return try {
            // Registar no Firebase Authentication
            val result = auth.createUserWithEmailAndPassword(volunteer.email, volunteer.password).await()
            val userId = result.user?.uid ?: throw Exception("User ID não encontrado")

            // Guardar detalhes do voluntário no Realtime Database
            volunteersRef.child(userId).setValue(volunteer.copy(password = "")).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loginVolunteer(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Funcao para adicionar beneficiarios
    suspend fun addBeneficiary(beneficiary: Beneficiary): Result<Unit> {
        return try {
            beneficiariesRef.child(beneficiary.id).setValue(beneficiary).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Funcao para extrair beneficiarios da firebase
    fun getBeneficiaries(): Flow<List<Beneficiary>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val beneficiaries = snapshot.children.mapNotNull { it.getValue(Beneficiary::class.java) }
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
}
