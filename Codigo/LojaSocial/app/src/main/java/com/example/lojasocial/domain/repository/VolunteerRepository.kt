package com.example.lojasocial.domain.repository

import com.example.lojasocial.domain.model.Volunteer
import com.example.lojasocial.domain.model.VolunteerLogin

interface VolunteerRepository {
    suspend fun registerVolunteer(volunteer: Volunteer): Result<Volunteer>
    suspend fun loginVolunteer(volunteerLogin: VolunteerLogin): Result<Volunteer>
    suspend fun getAllVolunteers(): kotlinx.coroutines.flow.Flow<List<Volunteer>>
    suspend fun updateVolunteer(volunteer: Volunteer): Result<Unit>
    suspend fun deleteVolunteer(volunteerId: String): Result<Unit>
}
