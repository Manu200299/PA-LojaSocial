package com.example.lojasocial.data.repository

import com.example.lojasocial.data.model.Volunteer
import com.example.lojasocial.data.remote.FirebaseApi
import com.example.lojasocial.data.remote.FirebaseVolunteerApi
import com.example.lojasocial.domain.repository.VolunteerRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class VolunteerRepositoryImpl(
    private val firebaseApi: FirebaseApi
) : VolunteerRepository {

    override suspend fun registerVolunteer(volunteer: Volunteer): Result<Unit> {
        return firebaseApi.registerVolunteer(volunteer)
    }

    override suspend fun loginVolunteer(email: String, password: String): Result<Unit> {
        return firebaseApi.loginVolunteer(email, password)
    }
}
