package com.znu.personalfinancetracker.ui.view.categories

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import com.znu.personalfinancetracker.data.model.category.CategoryRepositoryImpl
import com.znu.personalfinancetracker.ui.theme.PersonalFinanceTrackerTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ParameterizedCategoryNameValidationTest(
    private val name: String,
    private val isErrorExpected: Boolean
) {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        val viewModel = CategoriesViewModel(CategoryRepositoryImpl())
        composeTestRule.setContent {
            PersonalFinanceTrackerTheme {
                CategoryManagerView(viewModel = viewModel)
            }
        }
    }

    @Test
    fun categoryNameValidation() {
        composeTestRule.onNodeWithText("Category name").performTextInput("Т")
        composeTestRule.onNodeWithText("Category name").performTextReplacement(name)

        val error = composeTestRule.onNodeWithText("The field cannot be empty")
        val saveButton = composeTestRule.onNodeWithText("Save")
        if (isErrorExpected) {
            error.assertIsDisplayed()
            saveButton.assertIsNotEnabled()
        } else {
            error.assertDoesNotExist()
            saveButton.assertIsEnabled()
        }
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: name = \"{0}\" -> error {1}")
        fun data(): Collection<Array<Any>> = listOf(
            arrayOf("", true),
            arrayOf("Транспорт", false),
            arrayOf("Їжа", false)
        )
    }
}
