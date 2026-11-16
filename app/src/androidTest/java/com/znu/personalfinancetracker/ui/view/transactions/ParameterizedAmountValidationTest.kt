package com.znu.personalfinancetracker.ui.view.transactions

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import com.znu.personalfinancetracker.data.model.category.CategoryRepositoryImpl
import com.znu.personalfinancetracker.data.model.transaction.TransactionRepositoryImpl
import com.znu.personalfinancetracker.ui.theme.PersonalFinanceTrackerTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ParameterizedAmountValidationTest(
    private val amount: String,
    private val isErrorExpected: Boolean
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
                TransactionManagerView(viewModel = viewModel)
            }
        }
    }

    @Test
    fun amountValidation() {
        composeTestRule.onNodeWithText("Amount (₴)").performTextInput(amount)

        val error = composeTestRule.onNodeWithText("Enter a valid number")
        if (isErrorExpected) error.assertIsDisplayed() else error.assertDoesNotExist()
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: amount = {0} -> error {1}")
        fun data(): Collection<Array<Any>> = listOf(
            arrayOf("abc", true),
            arrayOf("12a", true),
            arrayOf("1,5", true),
            arrayOf("100", false),
            arrayOf("99.5", false),
            arrayOf("0", false)
        )
    }
}
