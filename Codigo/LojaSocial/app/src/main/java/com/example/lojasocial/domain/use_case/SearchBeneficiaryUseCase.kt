package com.example.lojasocial.domain.use_case

import com.example.lojasocial.data.model.Beneficiary
import com.example.lojasocial.domain.repository.BeneficiaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchBeneficiaryUseCase(
    private val repository: BeneficiaryRepository
) {
//    fun searchByFamily(query: String): Flow<List<Beneficiary>> {
//        return repository.getBeneficiaries().map { beneficiaries ->
//            beneficiaries.filter {
//
//            }
//        }
//    }

    fun searchByPhoneNumber(number: String): Flow<List<Beneficiary>> {
        return repository.getBeneficiaries().map { beneficiaries ->
            beneficiaries.filter {
                it.telefone.contains(number)
            }
        }
    }


    fun searchByIdNumber(id: String): Flow<List<Beneficiary>> {
        return repository.getBeneficiaries().map { beneficiaries ->
            beneficiaries.filter {
                it.numeroIdentificacao.contains(id)
            }
        }
    }
}