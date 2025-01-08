package com.example.lojasocial.data.remote.model

import com.example.lojasocial.domain.model.Visit
import com.example.lojasocial.domain.model.VisitItem
import java.util.Date

data class VisitDto(
    var id: String = "",
    var beneficiaryId: String = "",
    var startTime: Long = 0,
    var endTime: Long? = null,
    var items: List<VisitItemDto> = emptyList()
) {
    fun toVisit(): Visit {
        return Visit(
            id = id,
            beneficiaryId = beneficiaryId,
            startTime = Date(startTime),
            endTime = endTime?.let { Date(it) },
            items = items.map { it.toVisitItem() }
        )
    }
}

data class VisitItemDto(
    var stockItemId: String = "",
    var quantity: Int = 0
) {
    fun toVisitItem(): VisitItem {
        return VisitItem(
            stockItemId = stockItemId,
            quantity = quantity
        )
    }
}

fun Visit.toVisitDto(): VisitDto {
    return VisitDto(
        id = id,
        beneficiaryId = beneficiaryId,
        startTime = startTime.time,
        endTime = endTime?.time,
        items = items.map { it.toVisitItemDto() }
    )
}

fun VisitItem.toVisitItemDto(): VisitItemDto {
    return VisitItemDto(
        stockItemId = stockItemId,
        quantity = quantity
    )
}