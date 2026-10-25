package com.znu.personalfinancetracker.data.model.category

interface CategoryRepository {
    fun getAllCategories(): List<Category>
    fun getCategoryById(id: String): Category?
    fun getExpenseCategories(): List<Category>
    fun getIncomeCategories(): List<Category>
    fun addCategory(name: String, description: String?, isExpense: Boolean): Category
    fun updateCategory(category: Category): Boolean
    fun deleteCategory(id: String): Boolean
}