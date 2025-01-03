package com.example.lojasocial.data.model

import com.google.firebase.database.IgnoreExtraProperties
import java.util.Date

// Data model dos Beneficiarios
@IgnoreExtraProperties
data class Beneficiary(
    var id: String = "",
    var nome: String = "",
    var nacionalidade: String = "",
    var dataNascimento: String = "",
    var telefone: String = "",
    var numeroIdentificacao: String = "",
    var freguesia: String = "",
    var cidade: String = "",
    var contadorVisitas: Int = 0,
    var prioridade: String = "", // Default value added
    var escola: String = "",
    var anoEscolar: String = ""
) {
    // No-argument constructor for Firebase
    constructor() : this("", "", "", "", "", "", "", "", 0, "", "", "")
}
