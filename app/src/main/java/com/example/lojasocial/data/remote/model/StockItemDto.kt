package com.example.lojasocial.data.remote.model

import com.example.lojasocial.domain.model.StockItem
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class StockItemDto(
    var itemId: String = "",
    var nome: String = "",
    var quantidade: Int = 0,
    var categoria: String = "",
    var isEssential: Boolean = false
) {
    // Construtor vazio (necessário para o Firebase)
    constructor() : this("", "", 0, "", false)

    // Converte StockItemDto -> StockItem
    fun toStockItem(): StockItem {
        return StockItem(
            itemId = itemId,
            nome = nome,
            quantidade = quantidade,
            categoria = categoria,
            isEssential = isEssential
        )
    }
}

// Converte StockItem -> StockItemDto
fun StockItem.toStockItemDto(): StockItemDto {
    return StockItemDto(
        itemId = itemId,
        nome = nome,
        quantidade = quantidade,
        categoria = categoria,
        isEssential = isEssential
    )
}
