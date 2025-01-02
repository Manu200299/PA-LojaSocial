package com.example.lojasocial.data.model

import com.google.firebase.database.IgnoreExtraProperties
import java.util.Date

@IgnoreExtraProperties
data class Beneficiary(
    val id: String = "",
    val nome: String = "",
)
