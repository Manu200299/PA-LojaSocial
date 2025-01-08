package com.example.lojasocial.data.repository

import com.example.lojasocial.data.remote.api.FirebaseApi
import com.example.lojasocial.data.remote.model.toVisitDto
import com.example.lojasocial.domain.model.Beneficiary
import com.example.lojasocial.domain.model.Visit
import com.example.lojasocial.domain.repository.VisitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class VisitRepositoryImpl(private val api: FirebaseApi): VisitRepository {
    override suspend fun createVisit(visit: Visit): Result<Visit>{
        return api.createVisit(visit.toVisitDto()).map { it.toVisit() }
    }

    override suspend fun updateVisit(visit: Visit): Result<Unit>{
        return api.updateVisit(visit.toVisitDto())
    }

    override suspend fun getVisitsByBeneficiaryId(beneficiaryId: String): Flow<List<Visit>> {
        return api.getVisitsByBeneficiaryId(beneficiaryId).map { dtoList ->
            dtoList.map { it.toVisit() }
        }
    }

    override suspend fun getActiveVisitForBeneficiary(beneficiaryId: String): Visit? {
        return api.getActiveVisitForBeneficiary(beneficiaryId)?.toVisit()
    }

    override suspend fun finalizeVisit(visitId: String): Result<Unit> {
        return api.finalizeVisit(visitId)
    }

    override suspend fun getVisitById(visitId: String): Visit? {
        return api.getVisitById(visitId)?.toVisit()
    }
}

