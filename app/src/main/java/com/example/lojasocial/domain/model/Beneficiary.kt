package com.example.lojasocial.domain.model

import com.example.lojasocial.data.remote.model.BeneficiaryDto

data class Beneficiary(
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
    // Adicionar data de registo
) {
    fun toBeneficiaryDto(): BeneficiaryDto{
        return BeneficiaryDto(
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
}

