package com.znu.personalfinancetracker.data.model.transaction

import com.znu.personalfinancetracker.data.model.category.Category

interface TransactionRepository {
    fun getAllTransactions(): List<Transaction>
    fun getTransactionById(id: String): Transaction?
    fun getTransactionsByCategory(categoryId: String): List<Transaction>
    fun getTransactionsByDateRange(startDate: String, endDate: String): List<Transaction>

    fun addTransaction(
        date: String,
        description: String,
        amount: Double,
        category: Category
    ): Transaction

    fun updateTransaction(transaction: Transaction): Boolean
    fun deleteTransaction(id: String): Boolean
}
