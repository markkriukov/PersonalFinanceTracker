package com.znu.personalfinancetracker.ui.view.categories

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.znu.personalfinancetracker.data.model.category.CategoryRepositoryImpl
import com.znu.personalfinancetracker.ui.theme.PersonalFinanceTrackerTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ParameterizedCategoriesViewTest(
    private val categoryName: String
) {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        val viewModel = CategoriesViewModel(CategoryRepositoryImpl())
        composeTestRule.setContent {
            PersonalFinanceTrackerTheme {
                CategoriesView(viewModel = viewModel)
            }
        }
    }

    @Test
    fun removeCategoryDeletesOnlyIt() {
        composeTestRule.onNode(
            hasContentDescription("Remove category") and hasAnyAncestor(hasText(categoryName))
        ).performClick()

        composeTestRule.onNodeWithText(categoryName).assertDoesNotExist()
        (ALL_CATEGORIES - categoryName).forEach { name ->
            composeTestRule.onNodeWithText(name).assertIsDisplayed()
        }
    }

    companion object {
        private val ALL_CATEGORIES = listOf("Зарплата", "Продукти", "Подарунки", "Інше")

        @JvmStatic
        @Parameterized.Parameters(name = "{index}: remove {0}")
        fun data(): Collection<Array<Any>> = ALL_CATEGORIES.map { arrayOf(it) }
    }
}
