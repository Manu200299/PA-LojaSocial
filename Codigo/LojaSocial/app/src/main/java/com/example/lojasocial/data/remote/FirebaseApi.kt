package com.example.lojasocial.data.remote

import com.example.lojasocial.data.model.Beneficiary
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

// Calls para a API da Firebase
class FirebaseApi(private val database: FirebaseDatabase) {

    // Nome da table beneficiarios no firebase
    private val beneficiariesRef = database.getReference("Beneficiarios")

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
