package com.example.lojasocial.data.repository

import com.example.lojasocial.data.local.SessionManager
import com.example.lojasocial.data.remote.model.BeneficiaryDto
import com.example.lojasocial.data.remote.api.FirebaseApi
import com.example.lojasocial.domain.model.Beneficiary
import com.example.lojasocial.domain.repository.BeneficiaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Repositorio beneficiario da camada de dados
class BeneficiaryRepositoryImpl(
    private val api: FirebaseApi,
//    private val sessionManager: SessionManager
) : BeneficiaryRepository {

    // Funcao para adicionar beneficiario
    override suspend fun addBeneficiary(addBeneficiary: Beneficiary): Result<Unit> {
        return api.addBeneficiary(addBeneficiary.toBeneficiaryDto())
    }


    // Funcao para extrair beneficiarios da firebase
    override suspend fun getBeneficiaries(): Flow<List<Beneficiary>> {
        return api.getBeneficiaries().map { dtoList ->
            dtoList.map { it.toBeneficiary() }
        }
    }

    // Funcao para pesquisar beneficiario por numero de telemovel
    override suspend fun searchByPhoneNumber(number: String): Flow<List<Beneficiary>> {
        return api.getBeneficiaries().map { dtoList ->
            dtoList.map { it.toBeneficiary() } .filter {
                it.telefone.contains(number)
            }
        }
    }

    // Funcao para pesquisar beneficiario por numero de identificacao
    override suspend fun searchByIdNumber(id: String): Flow<List<Beneficiary>> {
        return api.getBeneficiaries().map { dtoList ->
            dtoList.map { it.toBeneficiary() } .filter {
                it.numeroIdentificacao.contains(id)
            }
        }
    }

    // TODO Funcao para pesquisar beneficiario por familia
}
