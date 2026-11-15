package com.znu.personalfinancetracker.data.model.category

import java.util.UUID

class CategoryRepositoryImpl: CategoryRepository {
    private val _categories = mutableListOf(
        Category(UUID.randomUUID().toString(), "Зарплата", null, false),
        Category(UUID.randomUUID().toString(), "Продукти", null, true),
        Category(UUID.randomUUID().toString(), "Подарунки", null, true),
        Category(UUID.randomUUID().toString(), "Інше", null, true)
    )

    val categories: List<Category> = _categories

    override fun getAllCategories(): List<Category> {
        return categories.toList()
    }

    override fun getCategoryById(id: String): Category? {
        return categories.find { it.id == id }
    }

    override fun getExpenseCategories(): List<Category> {
        Thread.sleep(2000)
        return categories.filter { it.isExpense }
    }

    override fun getIncomeCategories(): List<Category> {
        return categories.filter { !it.isExpense }
    }

    override fun addCategory(
        name: String,
        description: String?,
        isExpense: Boolean
    ): Category {
        if (name.isBlank()) {
            throw IllegalArgumentException("Назва категорії не може бути порожньою")
        }
        val newId = UUID.randomUUID().toString()
        val newCategory = Category(id = newId, name = name, description = description, isExpense = isExpense)
        _categories.add(newCategory)
        return newCategory
    }

    override fun updateCategory(category: Category): Boolean {
        val index = _categories.indexOfFirst { it.id == category.id }
        return if (index != -1) {
            _categories[index] = category
            true
        } else {
            false
        }
    }

    override fun deleteCategory(id: String): Boolean =
        _categories.removeIf { it.id == id }
}
