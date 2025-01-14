package com.example.lojasocial.domain.use_case

import android.util.Log
import com.example.lojasocial.domain.model.Volunteer
import com.example.lojasocial.domain.model.VolunteerLogin
import com.example.lojasocial.domain.repository.VolunteerRepository
import kotlinx.coroutines.flow.Flow

class VolunteerLoginUseCase(private val repository: VolunteerRepository) {
    suspend operator fun invoke(volunteerLogin: VolunteerLogin): Result<Volunteer> {
        Log.d("VolunteerUseCase", "Logging in volunteer...: $volunteerLogin")
        return repository.loginVolunteer(volunteerLogin)
    }
}

class VolunteerRegisterUseCase(private val repository: VolunteerRepository) {
    suspend operator fun invoke(volunteer: Volunteer): Result<Volunteer> {
        Log.d("VolunteerUseCase", "Registering volunteer...: $volunteer")
        return repository.registerVolunteer(volunteer)
    }
}

class GetAllVolunteersUseCase(private val repository: VolunteerRepository) {
    suspend operator fun invoke(): Flow<List<Volunteer>> {
        return repository.getAllVolunteers()
    }
}

class UpdateVolunteerUseCase(private val repository: VolunteerRepository) {
    suspend operator fun invoke(volunteer: Volunteer): Result<Unit> {
        return repository.updateVolunteer(volunteer)
    }
}

class DeleteVolunteerUseCase(private val repository: VolunteerRepository) {
    suspend operator fun invoke(volunteerId: String): Result<Unit> {
        return repository.deleteVolunteer(volunteerId)
    }
}



