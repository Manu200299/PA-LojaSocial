package com.example.lojasocial.domain.repository

import com.example.lojasocial.domain.model.Volunteer
import com.example.lojasocial.domain.model.VolunteerLogin

interface VolunteerRepository {
    suspend fun registerVolunteer(volunteer: Volunteer): Result<Unit>
    suspend fun loginVolunteer(volunteerLogin: VolunteerLogin): Result<Volunteer>
}
