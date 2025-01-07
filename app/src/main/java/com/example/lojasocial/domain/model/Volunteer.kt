package com.example.lojasocial.domain.model

import com.example.lojasocial.data.remote.model.VolunteerDto
import com.example.lojasocial.data.remote.model.VolunteerLoginDto
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Volunteer(
    var volunteerId: String = "",
    var escala: String = "default",
    var nome: String = "",
    var telefone: String = "",
    var password: String = "",
    var tipo: String = "voluntario" // Padrao como "voluntario"
){
    fun toVolunteerDto(): VolunteerDto{
        return VolunteerDto(volunteerId = volunteerId, escala = escala, nome = nome, telefone = telefone, password = password, tipo = tipo)
    }
}

data class VolunteerLogin(
    val telefone: String,
    val password: String
){
    fun toVolunteerLoginDto(): VolunteerLoginDto {
        return VolunteerLoginDto(telefone = telefone, password = password)
    }
}