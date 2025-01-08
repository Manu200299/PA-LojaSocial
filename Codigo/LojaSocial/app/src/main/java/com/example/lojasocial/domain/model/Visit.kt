package com.example.lojasocial.domain.model

import java.util.Date

data class Visit(
    val id: String = "",
    val beneficiaryId: String,
    val startTime: Date = Date(),
    val endTime: Date? = null,
    val items: List<VisitItem> = emptyList()
)

data class VisitItem(
    val stockItemId: String,
    val quantity: Int
)