package com.znu.personalfinancetracker.ui.view.transactions

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.znu.personalfinancetracker.data.model.category.Category
import com.znu.personalfinancetracker.data.model.category.CategoryRepository
import com.znu.personalfinancetracker.data.model.transaction.Transaction
import com.znu.personalfinancetracker.data.model.transaction.TransactionRepository

class TransactionsViewModel(
    private val categoryRepository: CategoryRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {
    private val _categories = MutableLiveData<List<Category>>().apply {
        value = categoryRepository.getAllCategories()
    }
    val categories: LiveData<List<Category>> = _categories

    private val _transactions = MutableLiveData<List<Transaction>>().apply {
        value = transactionRepository.getAllTransactions()
    }
    val transactions: LiveData<List<Transaction>> = _transactions

    // Form fields
    var description by mutableStateOf("")
        private set
    var amount by mutableStateOf("")
        private set
    var date by mutableStateOf("")
        private set
    var selectedCategory by mutableStateOf(_categories.value?.firstOrNull())
        private set
    var expanded by mutableStateOf(false)
        private set
    var dateError by mutableStateOf(false)
        private set
    var amountError by mutableStateOf(false)
        private set

    fun toggleExpanded() {
        expanded = !expanded
    }

    fun updateDescription(newDescription: String) {
        description = newDescription
    }

    fun updateAmount(newAmount: String) {
        amount = newAmount
        amountError = newAmount.isNotBlank() && newAmount.toDoubleOrNull() == null
    }

    fun updateDate(newDate: String) {
        date = newDate
        dateError = newDate.isNotBlank() && !isValidDate(newDate)
    }

    fun updateCategory(category: Category) {
        selectedCategory = category
    }

    private fun isValidDate(input: String): Boolean {
        val regex = Regex("""^\d{2}\.\d{2}\.\d{4}$""")
        return regex.matches(input)
    }

    fun addTransaction() {
        val descriptionValue = description
        val amountValueString = amount
        val dateValue = date
        val selectedCategoryValue = selectedCategory ?: return

        if (descriptionValue.isBlank() || amountValueString.isBlank() || dateValue.isBlank()) {
            return
        }

        if (dateError || amountError) {
            return
        }

        val amountValue = amountValueString.toDoubleOrNull() ?: return
        val signedAmount = if (selectedCategoryValue.isExpense) -amountValue else amountValue

        val newTransaction = transactionRepository.addTransaction(
            date = dateValue, // e.g., "05.04.2025"
            description = descriptionValue,
            amount = signedAmount,
            category = selectedCategoryValue
        )

        _transactions.value = _transactions.value.orEmpty() + newTransaction

        cancelTransaction()
    }

    fun cancelTransaction() {
        description = ""
        amount = ""
        date = ""
        selectedCategory = _categories.value?.firstOrNull()
        expanded = false
        dateError = false
        amountError = false
    }

    fun deleteTransaction(transactionId: String) {
        transactionRepository.deleteTransaction(transactionId)
        _transactions.value = _transactions.value.orEmpty().filter { it.id != transactionId }
    }

    fun updateTransaction(transaction: Transaction) {
        transactionRepository.updateTransaction(transaction)
        _transactions.value = _transactions.value.orEmpty().map {
            if (it.id == transaction.id) transaction else it
        }
    }
}
