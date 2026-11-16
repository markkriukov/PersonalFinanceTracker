package com.znu.personalfinancetracker.ui.view.transactions

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.znu.personalfinancetracker.data.model.category.CategoryRepositoryImpl
import com.znu.personalfinancetracker.data.model.transaction.TransactionRepositoryImpl
import com.znu.personalfinancetracker.ui.theme.PersonalFinanceTrackerTheme
import com.znu.personalfinancetracker.ui.view.categories.CategoriesViewModel
import com.znu.personalfinancetracker.ui.view.main.MainView
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TransactionsViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var transactionsViewModel: TransactionsViewModel
    private lateinit var categoriesViewModel: CategoriesViewModel

    @Before
    fun setUp() {
        composeTestRule.runOnUiThread {
            val categoryRepository = CategoryRepositoryImpl()
            transactionsViewModel = TransactionsViewModel(
                categoryRepository,
                TransactionRepositoryImpl(categoryRepository)
            )
            categoriesViewModel = CategoriesViewModel(categoryRepository)
        }
    }

    private fun showTransactionsView() {
        composeTestRule.setContent {
            PersonalFinanceTrackerTheme {
                TransactionsView(viewModel = transactionsViewModel)
            }
        }
    }

    private fun showMainView() {
        composeTestRule.setContent {
            PersonalFinanceTrackerTheme {
                MainView(
                    transactionsViewModel = transactionsViewModel,
                    categoriesViewModel = categoriesViewModel
                )
            }
        }
    }

    @Test
    fun titleAndInitialTransactionsAreDisplayed() {
        showTransactionsView()

        composeTestRule.onNodeWithText("MY FINANCES").assertIsDisplayed()
        composeTestRule.onNodeWithText("ФОП").assertIsDisplayed()
        composeTestRule.onNodeWithText("Вода").assertIsDisplayed()
        composeTestRule.onNodeWithText("Квіти").assertIsDisplayed()
        composeTestRule.onNodeWithText("Шоколад").assertIsDisplayed()
        composeTestRule.onNodeWithText("01.02.2025").assertIsDisplayed()
        composeTestRule.onNodeWithText("10.02.2025").assertIsDisplayed()
        composeTestRule.onAllNodesWithText("15.02.2025").assertCountEquals(2)
    }

    @Test
    fun transactionsShowSignedAmounts() {
        showTransactionsView()

        composeTestRule.onNodeWithText("+25000.0 ₴").assertIsDisplayed()
        composeTestRule.onNodeWithText("-1500.0 ₴").assertIsDisplayed()
        composeTestRule.onNodeWithText("-200.0 ₴").assertIsDisplayed()
        composeTestRule.onNodeWithText("-100.0 ₴").assertIsDisplayed()
    }

    @Test
    fun addTransactionButtonOpensNewTransactionScreen() {
        showMainView()

        composeTestRule.onNodeWithContentDescription("Add Transaction").performClick()

        composeTestRule.onNodeWithText("NEW TRANSACTION").assertIsDisplayed()
    }

    @Test
    fun transactionClickOpensEditTransactionScreen() {
        showMainView()

        composeTestRule.onNodeWithText("Вода").performClick()

        composeTestRule.onNodeWithText("EDIT TRANSACTION").assertIsDisplayed()
        composeTestRule.onNodeWithText("Description").assertTextContains("Вода")
    }
}
