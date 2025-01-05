package com.example.lojasocial.data.remote

import com.example.lojasocial.data.model.Volunteer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class FirebaseVolunteerApi(
    private val auth: FirebaseAuth,
    private val database: FirebaseDatabase
) {

    private val volunteersRef = database.getReference("Voluntarios")

    suspend fun registerVolunteer(volunteer: Volunteer): Result<Unit> {
        return try {
            // Registra no Firebase Authentication
            val result = auth.createUserWithEmailAndPassword(volunteer.email, volunteer.password).await()
            val userId = result.user?.uid ?: throw Exception("Erro ao criar conta")

            // Adiciona no Realtime Database
            volunteersRef.child(userId).setValue(volunteer.copy(id = userId)).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loginVolunteer(email: String, senha: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, senha).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
