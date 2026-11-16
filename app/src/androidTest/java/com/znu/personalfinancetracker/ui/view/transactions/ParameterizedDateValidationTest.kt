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
class ParameterizedDateValidationTest(
    private val date: String,
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
    fun dateValidation() {
        composeTestRule.onNodeWithText("Date (DD.MM.YYYY)").performTextInput(date)

        val error = composeTestRule.onNodeWithText("Format: DD.MM.YYYY")
        if (isErrorExpected) error.assertIsDisplayed() else error.assertDoesNotExist()
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: date = {0} -> error {1}")
        fun data(): Collection<Array<Any>> = listOf(
            arrayOf("2025-01-01", true),
            arrayOf("1.1.2025", true),
            arrayOf("abc", true),
            arrayOf("01.01.2025", false),
            arrayOf("15.02.2025", false)
        )
    }
}
