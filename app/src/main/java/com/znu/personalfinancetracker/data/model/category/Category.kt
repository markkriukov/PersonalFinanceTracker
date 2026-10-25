package com.znu.personalfinancetracker.data.model.category

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: String,
    val name: String,
    val description: String? = null,
    val isExpense: Boolean
)
