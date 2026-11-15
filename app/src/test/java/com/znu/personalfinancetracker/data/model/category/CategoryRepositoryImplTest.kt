package com.znu.personalfinancetracker.data.model.category

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CategoryRepositoryImplTest {

    private lateinit var repository: CategoryRepositoryImpl
    private lateinit var initialCategories: List<Category>
    private lateinit var existingCategory: Category

    @Before
    fun setUp() {
        repository = CategoryRepositoryImpl()
        initialCategories = repository.getAllCategories()
        existingCategory = initialCategories.first { it.name == "Продукти" }
    }

    @Test
    fun getAllCategories() {
        val result = repository.getAllCategories()

        assertEquals(4, result.size)
        assertEquals(
            listOf("Зарплата", "Продукти", "Подарунки", "Інше"),
            result.map { it.name }
        )
    }

    @Test
    fun getCategoryById() {
        val result = repository.getCategoryById(existingCategory.id)

        assertNotNull(result)
        assertEquals(existingCategory, result)
    }

    @Test
    fun getExpenseCategories() {
        val result = repository.getExpenseCategories()

        assertEquals(3, result.size)
        assertTrue(result.all { it.isExpense })
        assertEquals(
            listOf("Продукти", "Подарунки", "Інше"),
            result.map { it.name }
        )
    }

    @Test
    fun getIncomeCategories() {
        val result = repository.getIncomeCategories()

        assertEquals(1, result.size)
        assertFalse(result.first().isExpense)
        assertEquals("Зарплата", result.first().name)
    }

    @Test
    fun addCategory() {
        val added = repository.addCategory(
            name = "Транспорт",
            description = "Проїзд у громадському транспорті",
            isExpense = true
        )

        assertTrue(added.id.isNotBlank())
        assertEquals("Транспорт", added.name)
        assertEquals("Проїзд у громадському транспорті", added.description)
        assertTrue(added.isExpense)
        assertEquals(initialCategories.size + 1, repository.getAllCategories().size)
        assertEquals(added, repository.getCategoryById(added.id))
    }

    @Test
    fun updateCategory() {
        val updated = existingCategory.copy(
            name = "Продукти харчування",
            description = "Супермаркети та ринки"
        )

        val result = repository.updateCategory(updated)

        assertTrue(result)
        assertEquals(updated, repository.getCategoryById(existingCategory.id))
        assertEquals(initialCategories.size, repository.getAllCategories().size)
    }

    @Test
    fun deleteCategory() {
        val result = repository.deleteCategory(existingCategory.id)

        assertTrue(result)
        assertNull(repository.getCategoryById(existingCategory.id))
        assertEquals(initialCategories.size - 1, repository.getAllCategories().size)
    }
}
