package com.example.lojasocial.data.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Volunteer(
    var id: String = "",
    var nome: String = "",
    var email: String = "",
    var telefone: String = "",
    var senha: String = "",
    var dataNascimento: String = "",
    var tipo: String = "voluntário" // Padrão como "voluntário"
)
