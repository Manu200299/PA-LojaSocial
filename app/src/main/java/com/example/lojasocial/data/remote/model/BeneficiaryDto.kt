package com.example.lojasocial.data.remote.model

import com.example.lojasocial.domain.model.Beneficiary
import com.google.firebase.database.IgnoreExtraProperties

// Data model dos Beneficiarios
@IgnoreExtraProperties
data class BeneficiaryDto(
    var id: String = "",
    var nome: String = "",
    var nacionalidade: String = "",
    var dataNascimento: String = "",
    var telefone: String = "",
    var numeroIdentificacao: String = "",
    var freguesia: String = "",
    var cidade: String = "",
    var contadorVisitas: Int = 0, // Default value added
    var prioridade: String = "",
    var escola: String = "",
    var anoEscolar: String = ""
) {
    fun toBeneficiary(): Beneficiary{
        return Beneficiary(
            id = id,
            nome = nome,
            nacionalidade = nacionalidade,
            dataNascimento = dataNascimento,
            telefone = telefone,
            numeroIdentificacao = numeroIdentificacao,
            freguesia = freguesia,
            cidade = cidade,
            contadorVisitas = contadorVisitas,
            prioridade = prioridade,
            escola = escola,
            anoEscolar = anoEscolar
        )
    }

    // No-argument constructor for Firebase
    constructor() : this("", "", "", "", "", "", "", "", 0, "", "", "")
}
