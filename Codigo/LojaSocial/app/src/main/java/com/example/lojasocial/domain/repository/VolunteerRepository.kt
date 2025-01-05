package com.example.lojasocial.domain.repository

import com.example.lojasocial.data.model.Volunteer
import kotlinx.coroutines.flow.Flow

interface VolunteerRepository {
    suspend fun registerVolunteer(volunteer: Volunteer): Result<Unit>
    suspend fun loginVolunteer(email: String, password: String): Result<Unit>
}
