package com.znu.personalfinancetracker.data.model.transaction

import com.znu.personalfinancetracker.data.model.category.Category
import com.znu.personalfinancetracker.data.model.category.CategoryRepository
import java.util.UUID

class TransactionRepositoryImpl(
    categoryRepository: CategoryRepository
) : TransactionRepository {

    private val _transactions = mutableListOf(
        Transaction("1", "01.02.2025", "ФОП", 25000.0, categoryRepository.getAllCategories().component1()),
        Transaction("2", "10.02.2025", "Вода", 1500.0, categoryRepository.getAllCategories().component2()),
        Transaction("3", "15.02.2025", "Квіти", 200.0, categoryRepository.getAllCategories().component3()),
        Transaction("4", "15.02.2025", "Шоколад", 100.0, categoryRepository.getAllCategories().component3())
    )

    // Public immutable view
    val transactions: List<Transaction> = _transactions

    override fun getAllTransactions(): List<Transaction> = transactions.toList()

    override fun getTransactionById(id: String): Transaction? =
        transactions.find { it.id == id }

    override fun getTransactionsByCategory(categoryId: String): List<Transaction> =
        transactions.filter { it.category.id == categoryId }

    override fun getTransactionsByDateRange(startDate: String, endDate: String): List<Transaction> =
        transactions.filter { it.date in startDate..endDate }

    override fun addTransaction(
        date: String,
        description: String,
        amount: Double,
        category: Category
    ): Transaction {
        val newId = UUID.randomUUID().toString()
        val transaction = Transaction(
            id = newId,
            date = date,
            description = description,
            amount = amount,
            category = category
        )
        _transactions.add(transaction)
        return transaction
    }

    override fun updateTransaction(transaction: Transaction): Boolean {
        val index = _transactions.indexOfFirst { it.id == transaction.id }
        return if (index != -1) {
            _transactions[index] = transaction
            true
        } else {
            false
        }
    }

    override fun deleteTransaction(id: String): Boolean =
        _transactions.removeIf { it.id == id }
}