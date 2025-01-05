package com.example.lojasocial.domain.use_case

import android.R.attr.password
import com.example.lojasocial.data.model.Volunteer
import com.example.lojasocial.domain.repository.VolunteerRepository

class RegisterVolunteerUseCase(
    private val repository: VolunteerRepository
) {
    suspend operator fun invoke(
        nome: String,
        email: String,
        telefone: String,
        password: String,
        dataNascimento: String
    ): Result<Unit> {
        val volunteer = Volunteer(
            nome = nome,
            email = email,
            Telefone = telefone,
            password = password,
            dataNascimento = dataNascimento
        )
        return repository.registerVolunteer(volunteer)
    }
}
