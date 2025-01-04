package com.example.lojasocial.data.repository

import com.example.lojasocial.data.model.Volunteer
import com.example.lojasocial.data.remote.FirebaseVolunteerApi
import com.example.lojasocial.domain.repository.VolunteerRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class VolunteerRepositoryImpl(
    private val firebaseAuth: FirebaseAuth
) : VolunteerRepository {

    override suspend fun registerVolunteer(volunteer: Volunteer): Result<Unit> {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(volunteer.email, volunteer.senha).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loginVolunteer(email: String, senha: String): Result<Unit> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, senha).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
