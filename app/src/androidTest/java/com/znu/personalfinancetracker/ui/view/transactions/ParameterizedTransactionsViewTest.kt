package com.znu.personalfinancetracker.ui.view.transactions

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import com.znu.personalfinancetracker.data.model.category.CategoryRepositoryImpl
import com.znu.personalfinancetracker.data.model.transaction.TransactionRepositoryImpl
import com.znu.personalfinancetracker.ui.theme.PersonalFinanceTrackerTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ParameterizedTransactionsViewTest(
    private val description: String,
    private val expectedAmount: String
) {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        val viewModel = composeTestRule.runOnUiThread {
            val categoryRepository = CategoryRepositoryImpl()
            TransactionsViewModel(categoryRepository, TransactionRepositoryImpl(categoryRepository))
        }
        composeTestRule.setContent {
            PersonalFinanceTrackerTheme {
                TransactionsView(viewModel = viewModel)
            }
        }
    }

    @Test
    fun transactionShowsSignedAmount() {
        composeTestRule.onNode(hasText(description) and hasText(expectedAmount)).assertIsDisplayed()
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: {0} -> {1}")
        fun data(): Collection<Array<Any>> = listOf(
            arrayOf("ФОП", "+25000.0 ₴"),
            arrayOf("Вода", "-1500.0 ₴"),
            arrayOf("Квіти", "-200.0 ₴"),
            arrayOf("Шоколад", "-100.0 ₴")
        )
    }
}
