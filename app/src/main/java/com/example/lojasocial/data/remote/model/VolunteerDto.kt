package com.example.lojasocial.data.remote.model

import com.example.lojasocial.domain.model.Volunteer
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class VolunteerDto(
    var volunteerId: String = "",
    var escala: String = "default",
    var nome: String = "",
    var telefone: String = "",
    var password: String = "",
    var tipo: String = "voluntário" // Padrão como "voluntário"
){
    constructor() : this("", "", "", "", "", "")

    fun toVolunteer(): Volunteer{
        return Volunteer(volunteerId = volunteerId,
            escala = escala,
            nome = nome,
            telefone = telefone,
            tipo = tipo
        )
    }
}

data class VolunteerLoginDto(
    val telefone: String,
    val password: String
)
