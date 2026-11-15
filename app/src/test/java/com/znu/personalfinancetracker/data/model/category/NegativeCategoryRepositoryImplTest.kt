package com.znu.personalfinancetracker.data.model.category

import org.junit.Before
import org.junit.Test

class NegativeCategoryRepositoryImplTest {

    private lateinit var repository: CategoryRepositoryImpl

    @Before
    fun setUp() {
        repository = CategoryRepositoryImpl()
    }

    @Test(expected = IllegalArgumentException::class)
    fun addCategoryWithBlankName() {
        repository.addCategory(name = "   ", description = null, isExpense = true)
    }

    @Test(timeout = 1000)
    fun getExpenseCategoriesTimeout() {
        repository.getExpenseCategories()
    }
}
