package com.example.lojasocial.domain.repository

import com.example.lojasocial.domain.model.Beneficiary
import com.example.lojasocial.domain.model.Visit
import kotlinx.coroutines.flow.Flow

interface VisitRepository {
    suspend fun createVisit(visit: Visit): Result<Visit>
    suspend fun updateVisit(visit: Visit): Result<Unit>
    suspend fun getVisitsByBeneficiaryId(beneficiaryId: String): Flow<List<Visit>>
    suspend fun getActiveVisitForBeneficiary(beneficiaryId: String): Visit?
    suspend fun finalizeVisit(visitId: String): Result<Unit>
    suspend fun getVisitById(visitId: String): Visit?
}