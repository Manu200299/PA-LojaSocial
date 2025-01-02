package com.example.lojasocial.data.model

import com.google.firebase.database.IgnoreExtraProperties
import java.util.Date

// Data model dos Beneficiarios
@IgnoreExtraProperties
data class Beneficiary(
    val id: String = "",
    val nome: String = "",
    val nacionalidade: String = "",
    val dataNascimento: String = "",
    val telefone: String = "",
    val numeroIdentificacao: String = "",
    val freguesia: String = "",
    val cidade: String = "",
    val contadorVisitas: Int = 0,
    val prioridade: String,
    val escola: String = "",
    val anoEscolar: String = "",
)
