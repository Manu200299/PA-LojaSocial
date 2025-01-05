package com.example.lojasocial.domain.use_case

import com.example.lojasocial.domain.repository.VolunteerRepository

class LoginVolunteerUseCase(
    private val repository: VolunteerRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        return repository.loginVolunteer(email, password)
    }
}
