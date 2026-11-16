package com.znu.personalfinancetracker.ui.view.transactions

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
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
class ParameterizedSaveButtonTest(
    private val description: String,
    private val amount: String,
    private val date: String,
    private val isSaveEnabled: Boolean
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

    private fun inputIfNotEmpty(label: String, value: String) {
        if (value.isNotEmpty()) composeTestRule.onNodeWithText(label).performTextInput(value)
    }

    @Test
    fun saveButtonState() {
        inputIfNotEmpty("Description", description)
        inputIfNotEmpty("Amount (₴)", amount)
        inputIfNotEmpty("Date (DD.MM.YYYY)", date)

        val saveButton = composeTestRule.onNodeWithText("Save")
        if (isSaveEnabled) saveButton.assertIsEnabled() else saveButton.assertIsNotEnabled()
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: \"{0}\", \"{1}\", \"{2}\" -> Save enabled {3}")
        fun data(): Collection<Array<Any>> = listOf(
            arrayOf("", "100", "01.03.2025", false),
            arrayOf("Кава", "", "01.03.2025", false),
            arrayOf("Кава", "100", "", false),
            arrayOf("Кава", "abc", "01.03.2025", false),
            arrayOf("Кава", "100", "2025-03-01", false),
            arrayOf("Кава", "100", "01.03.2025", true)
        )
    }
}
