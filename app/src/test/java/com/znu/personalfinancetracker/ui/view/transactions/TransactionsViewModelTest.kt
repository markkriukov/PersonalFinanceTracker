package com.znu.personalfinancetracker.ui.view.transactions

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.znu.personalfinancetracker.data.model.category.Category
import com.znu.personalfinancetracker.data.model.category.CategoryRepository
import com.znu.personalfinancetracker.data.model.category.CategoryRepositoryImpl
import com.znu.personalfinancetracker.data.model.transaction.Transaction
import com.znu.personalfinancetracker.data.model.transaction.TransactionRepository
import com.znu.personalfinancetracker.data.model.transaction.TransactionRepositoryImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TransactionsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val salary = Category("1", "Зарплата", null, false)
    private val food = Category("2", "Продукти", null, true)
    private val testTransactions = listOf(
        Transaction("10", "01.02.2025", "ФОП", 25000.0, salary),
        Transaction("11", "10.02.2025", "Хліб", -50.0, food)
    )

    private lateinit var categoryRepository: CategoryRepository
    private lateinit var transactionRepository: TransactionRepository
    private lateinit var categoryRepositorySpy: CategoryRepository
    private lateinit var transactionRepositorySpy: TransactionRepository
    private lateinit var stubViewModel: TransactionsViewModel
    private lateinit var spyViewModel: TransactionsViewModel

    @Before
    fun setUp() {
        categoryRepository = mockk()
        transactionRepository = mockk()
        every { categoryRepository.getAllCategories() } returns listOf(salary, food)
        every { transactionRepository.getAllTransactions() } returns testTransactions
        stubViewModel = TransactionsViewModel(categoryRepository, transactionRepository)

        categoryRepositorySpy = spyk(CategoryRepositoryImpl())
        transactionRepositorySpy = spyk(TransactionRepositoryImpl(categoryRepositorySpy))
        spyViewModel = TransactionsViewModel(categoryRepositorySpy, transactionRepositorySpy)
    }

    @Test
    fun categoriesReturnsRepositoryData() {
        assertEquals(listOf(salary, food), stubViewModel.categories.value)
    }

    @Test
    fun transactionsReturnsRepositoryData() {
        assertEquals(testTransactions, stubViewModel.transactions.value)
    }

    @Test
    fun selectedCategoryReturnsFirstRepositoryCategory() {
        assertEquals(salary, stubViewModel.selectedCategory)
    }

    @Test
    fun selectedCategoryIsNullWhenRepositoryIsEmpty() {
        every { categoryRepository.getAllCategories() } returns emptyList()

        val viewModel = TransactionsViewModel(categoryRepository, transactionRepository)

        assertNull(viewModel.selectedCategory)
    }

    @Test
    fun addTransactionCallsRepositoryAdd() {
        val expenseCategory = spyViewModel.categories.value!!.first { it.name == "Продукти" }
        spyViewModel.updateDescription("Хліб")
        spyViewModel.updateAmount("50")
        spyViewModel.updateDate("20.02.2025")
        spyViewModel.updateCategory(expenseCategory)

        spyViewModel.addTransaction()

        verify(exactly = 1) {
            transactionRepositorySpy.addTransaction("20.02.2025", "Хліб", -50.0, expenseCategory)
        }
        assertTrue(spyViewModel.transactions.value!!.any { it.description == "Хліб" })
        assertEquals("", spyViewModel.description)
    }

    @Test
    fun addTransactionWithInvalidAmountDoesNotCallRepository() {
        spyViewModel.updateDescription("Хліб")
        spyViewModel.updateAmount("п'ятдесят")
        spyViewModel.updateDate("20.02.2025")

        spyViewModel.addTransaction()

        verify(exactly = 0) { transactionRepositorySpy.addTransaction(any(), any(), any(), any()) }
        assertTrue(spyViewModel.amountError)
    }

    @Test
    fun deleteTransactionCallsRepositoryDelete() {
        spyViewModel.deleteTransaction("1")

        verify(exactly = 1) { transactionRepositorySpy.deleteTransaction("1") }
        assertNull(spyViewModel.transactions.value!!.find { it.id == "1" })
    }

    @Test
    fun updateTransactionCallsRepositoryUpdate() {
        val updated = spyViewModel.transactions.value!!
            .first { it.id == "2" }
            .copy(description = "Вода і світло", amount = 1800.0)

        spyViewModel.updateTransaction(updated)

        verify(exactly = 1) { transactionRepositorySpy.updateTransaction(updated) }
        assertEquals(updated, spyViewModel.transactions.value!!.first { it.id == "2" })
    }
}
