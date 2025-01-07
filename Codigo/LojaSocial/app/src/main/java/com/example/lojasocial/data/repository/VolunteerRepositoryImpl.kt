package com.example.lojasocial.data.repository

import com.example.lojasocial.data.local.SessionManager
import com.example.lojasocial.data.remote.model.VolunteerDto
import com.example.lojasocial.data.remote.api.FirebaseApi
import com.example.lojasocial.domain.model.Volunteer
import com.example.lojasocial.domain.model.VolunteerLogin
import com.example.lojasocial.domain.repository.VolunteerRepository

class VolunteerRepositoryImpl(
    private val api: FirebaseApi,
    private val sessionManager: SessionManager
) : VolunteerRepository {

    // Funcao para registar voluntario
    override suspend fun registerVolunteer(volunteer: Volunteer): Result<Unit> {
        return try {
            val result = api.registerVolunteer(volunteer.toVolunteerDto())
            result.map { newVolunteerId ->
                volunteer.volunteerId = newVolunteerId
                Unit
            }
        } catch (e: Exception) {
            Result.failure(Exception("VolunteerRepositoryImpl | Error: $e"))
        }
    }

    // Funcao para logar voluntario
    override suspend fun loginVolunteer(volunteerLogin: VolunteerLogin): Result<Volunteer> {
        return try {
            val result = api.loginVolunteer(volunteerLogin.toVolunteerLoginDto())
            result.map { volunteerDto ->
                val loggedInVolunteer = volunteerDto.toVolunteer()
                // Guardar a sessao do voluntario
                sessionManager.saveVolunteerSession(
                    id = loggedInVolunteer.volunteerId,
                    phone = loggedInVolunteer.telefone,
                    name = loggedInVolunteer.nome
                )
                loggedInVolunteer
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // TODO implementar no use case
    suspend fun logout() {
        sessionManager.clearSession()
    }
}
