package com.example.lojasocial.data.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Volunteer(
    var id: String = "",
    var escala: String = "",
    var nome: String = "",
    var email: String = "",
    var Telefone: String = "",
    var password: String = "",
    var dataNascimento: String = "",
    var tipo: String = "voluntário" // Padrão como "voluntário"
)
