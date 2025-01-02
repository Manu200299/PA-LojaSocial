package com.example.lojasocial.domain.use_case

import com.example.lojasocial.data.model.Beneficiary
import com.example.lojasocial.domain.repository.BeneficiaryRepository
import java.util.SimpleTimeZone
import java.util.UUID
import javax.inject.Inject

// Use case para adicionar beneficiarios
class AddBeneficiaryUseCase(
    private val repository: BeneficiaryRepository
) {
    suspend operator fun invoke(
        nome: String,
        nacionalidade: String,
        dataNasacimento: String,
        telefone: String,
        numeroIdentificacao: String,
        freguesia: String,
        cidade: String,
        contadorVisitas: Int,
        prioridade: String,
        escola: String,
        anoEscolar: String
    ): Result<Unit> {
        val beneficiary = Beneficiary(
            id = UUID.randomUUID().toString(),
            nome = nome,
            nacionalidade = nacionalidade,
            dataNascimento = dataNasacimento,
            telefone = telefone,
            numeroIdentificacao = numeroIdentificacao,
            freguesia = freguesia,
            cidade = cidade,
            contadorVisitas = contadorVisitas,
            prioridade = prioridade,
            escola = escola,
            anoEscolar = anoEscolar
        )


        return repository.addBeneficiary(beneficiary)
    }
}

