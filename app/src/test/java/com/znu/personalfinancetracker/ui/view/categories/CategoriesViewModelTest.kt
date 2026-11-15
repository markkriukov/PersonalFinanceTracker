package com.znu.personalfinancetracker.ui.view.categories

import com.znu.personalfinancetracker.data.model.category.Category
import com.znu.personalfinancetracker.data.model.category.CategoryRepository
import com.znu.personalfinancetracker.data.model.category.CategoryRepositoryImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CategoriesViewModelTest {

    private val testCategories = listOf(
        Category("1", "Зарплата", null, false),
        Category("2", "Продукти", "Супермаркети", true)
    )

    private lateinit var categoryRepository: CategoryRepository
    private lateinit var categoryRepositorySpy: CategoryRepository
    private lateinit var stubViewModel: CategoriesViewModel
    private lateinit var spyViewModel: CategoriesViewModel

    @Before
    fun setUp() {
        categoryRepository = mockk()
        every { categoryRepository.getAllCategories() } returns testCategories
        stubViewModel = CategoriesViewModel(categoryRepository)

        categoryRepositorySpy = spyk(CategoryRepositoryImpl())
        spyViewModel = CategoriesViewModel(categoryRepositorySpy)
    }

    @Test
    fun categoriesReturnsRepositoryData() {
        assertEquals(testCategories, stubViewModel.categories.toList())
    }

    @Test
    fun categoriesIsEmptyWhenRepositoryIsEmpty() {
        every { categoryRepository.getAllCategories() } returns emptyList()

        val viewModel = CategoriesViewModel(categoryRepository)

        assertTrue(viewModel.categories.isEmpty())
    }

    @Test
    fun saveCategoryCallsRepositoryAdd() {
        spyViewModel.updateName("Транспорт")
        spyViewModel.updateDescription("Проїзд")
        spyViewModel.updateIsExpense(true)

        spyViewModel.saveCategory()

        verify(exactly = 1) { categoryRepositorySpy.addCategory("Транспорт", "Проїзд", true) }
        assertTrue(spyViewModel.categories.any { it.name == "Транспорт" })
        assertEquals("", spyViewModel.name.value)
    }

    @Test
    fun saveCategoryWithBlankNameDoesNotCallRepository() {
        spyViewModel.updateName("   ")

        spyViewModel.saveCategory()

        verify(exactly = 0) { categoryRepositorySpy.addCategory(any(), any(), any()) }
        assertTrue(spyViewModel.nameError.value)
    }

    @Test
    fun updateCategoryCallsRepositoryUpdate() {
        val updated = spyViewModel.categories.first().copy(name = "Заробітна плата")

        spyViewModel.updateCategory(updated)

        verify(exactly = 1) { categoryRepositorySpy.updateCategory(updated) }
        assertEquals(updated, spyViewModel.categories.first { it.id == updated.id })
    }

    @Test
    fun deleteCategoryCallsRepositoryDelete() {
        val category = spyViewModel.categories.first()

        spyViewModel.deleteCategory(category)

        verify(exactly = 1) { categoryRepositorySpy.deleteCategory(category.id) }
        assertFalse(spyViewModel.categories.contains(category))
    }
}
