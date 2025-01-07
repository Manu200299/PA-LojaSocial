package com.example.lojasocial.domain.use_case

import android.util.Log
import com.example.lojasocial.domain.model.Volunteer
import com.example.lojasocial.domain.model.VolunteerLogin
import com.example.lojasocial.domain.repository.VolunteerRepository

class VolunteerLoginUseCase(private val repository: VolunteerRepository) {
    suspend operator fun invoke(volunteerLogin: VolunteerLogin): Result<Volunteer> {
        Log.d("VolunteerUseCase", "Logging in volunteer...: $volunteerLogin")
        return repository.loginVolunteer(volunteerLogin)
    }
}

class VolunteerRegisterUseCase(private val repository: VolunteerRepository) {
    suspend operator fun invoke(volunteer: Volunteer): Result<Unit> {
        Log.d("VolunteerUseCase", "Registering volunteer...: $volunteer")
        return repository.registerVolunteer(volunteer)
    }
}
