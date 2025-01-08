
package com.example.lojasocial.domain.repository

interface StatisticsRepository {
    data class BeneficiaryStats(val month: String, val count: Int)
    data class NationalityStats(val country: String, val count: Int)
    data class ItemStats(val itemName: String, val percentage: Float)
}