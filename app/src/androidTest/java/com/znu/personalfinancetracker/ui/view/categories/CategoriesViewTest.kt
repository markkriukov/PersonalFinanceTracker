package com.znu.personalfinancetracker.ui.view.categories

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.znu.personalfinancetracker.data.model.category.CategoryRepositoryImpl
import com.znu.personalfinancetracker.ui.theme.PersonalFinanceTrackerTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CategoriesViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var viewModel: CategoriesViewModel

    @Before
    fun setUp() {
        viewModel = CategoriesViewModel(CategoryRepositoryImpl())
        composeTestRule.setContent {
            PersonalFinanceTrackerTheme {
                CategoriesView(viewModel = viewModel)
            }
        }
    }

    private fun removeButtonOf(name: String) = composeTestRule.onNode(
        hasContentDescription("Remove category") and hasAnyAncestor(hasText(name))
    )

    @Test
    fun titleCategoriesAndAddButtonAreDisplayed() {
        composeTestRule.onNodeWithText("CATEGORIES").assertIsDisplayed()
        composeTestRule.onNodeWithText("Зарплата").assertIsDisplayed()
        composeTestRule.onNodeWithText("Продукти").assertIsDisplayed()
        composeTestRule.onNodeWithText("Подарунки").assertIsDisplayed()
        composeTestRule.onNodeWithText("Інше").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Add Category").assertIsDisplayed()
    }

    @Test
    fun removeCategoryDeletesItFromList() {
        removeButtonOf("Продукти").performClick()

        composeTestRule.onNodeWithText("Продукти").assertDoesNotExist()
        composeTestRule.onNodeWithText("Зарплата").assertIsDisplayed()
        composeTestRule.onNodeWithText("Подарунки").assertIsDisplayed()
        composeTestRule.onNodeWithText("Інше").assertIsDisplayed()
    }
}
