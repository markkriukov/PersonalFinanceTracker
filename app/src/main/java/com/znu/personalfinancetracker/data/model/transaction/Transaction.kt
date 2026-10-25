package com.znu.personalfinancetracker.data.model.transaction

import com.znu.personalfinancetracker.data.model.category.Category
import kotlinx.serialization.Serializable

@Serializable
data class Transaction(
    val id: String,
    val date: String,
    val description: String,
    val amount: Double,
    val category: Category
)
