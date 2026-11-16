package com.znu.personalfinancetracker.ui.view.categories

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.znu.personalfinancetracker.data.model.category.Category
import com.znu.personalfinancetracker.data.model.category.CategoryRepositoryImpl
import com.znu.personalfinancetracker.ui.theme.PersonalFinanceTrackerTheme
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CategoryManagerViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var viewModel: CategoriesViewModel

    @Before
    fun setUp() {
        viewModel = CategoriesViewModel(CategoryRepositoryImpl())
    }

    private fun showCategoryManagerView(category: Category? = null) {
        composeTestRule.setContent {
            PersonalFinanceTrackerTheme {
                CategoryManagerView(viewModel = viewModel, categoryToEdit = category)
            }
        }
    }

    @Test
    fun newModeShowsTitleFieldsAndDisabledSave() {
        showCategoryManagerView()

        composeTestRule.onNodeWithText("NEW CATEGORY").assertIsDisplayed()
        composeTestRule.onNodeWithText("Category name").assertIsDisplayed()
        composeTestRule.onNodeWithText("Category description").assertIsDisplayed()
        composeTestRule.onNodeWithText("Type: Expense").assertIsDisplayed()
        composeTestRule.onNodeWithText("Save").assertIsNotEnabled()
    }

    @Test
    fun editModeShowsCategoryData() {
        showCategoryManagerView(Category("1", "Зарплата", "Щомісячна", false))

        composeTestRule.onNodeWithText("EDIT CATEGORY").assertIsDisplayed()
        composeTestRule.onNodeWithText("Category name").assertTextContains("Зарплата")
        composeTestRule.onNodeWithText("Category description").assertTextContains("Щомісячна")
        composeTestRule.onNodeWithText("Type: Income").assertIsDisplayed()
    }

    @Test
    fun switchTogglesCategoryType() {
        showCategoryManagerView()

        composeTestRule.onNode(isToggleable()).performClick()
        composeTestRule.onNodeWithText("Type: Income").assertIsDisplayed()

        composeTestRule.onNode(isToggleable()).performClick()
        composeTestRule.onNodeWithText("Type: Expense").assertIsDisplayed()
    }

    @Test
    fun emptyNameShowsErrorAndDisablesSave() {
        showCategoryManagerView()

        composeTestRule.onNodeWithText("Category name").performTextInput("Т")
        composeTestRule.onNodeWithText("Category name").performTextClearance()

        composeTestRule.onNodeWithText("The field cannot be empty").assertIsDisplayed()
        composeTestRule.onNodeWithText("Save").assertIsNotEnabled()
    }

    @Test
    fun validNameEnablesSave() {
        showCategoryManagerView()

        composeTestRule.onNodeWithText("Category name").performTextInput("Транспорт")

        composeTestRule.onNodeWithText("The field cannot be empty").assertDoesNotExist()
        composeTestRule.onNodeWithText("Save").assertIsEnabled()
    }

    @Test
    fun saveAddsCategoryAndClearsForm() {
        showCategoryManagerView()

        composeTestRule.onNodeWithText("Category name").performTextInput("Транспорт")
        composeTestRule.onNodeWithText("Save").performClick()

        composeTestRule.runOnIdle {
            assertTrue(viewModel.categories.any { it.name == "Транспорт" })
        }
        composeTestRule.onNodeWithText("Транспорт").assertDoesNotExist()
        composeTestRule.onNodeWithText("Save").assertIsNotEnabled()
    }

    @Test
    fun cancelClearsForm() {
        showCategoryManagerView()

        composeTestRule.onNodeWithText("Category name").performTextInput("Транспорт")
        composeTestRule.onNodeWithText("Category description").performTextInput("Таксі")
        composeTestRule.onNodeWithText("Cancel").performClick()

        composeTestRule.onNodeWithText("Транспорт").assertDoesNotExist()
        composeTestRule.onNodeWithText("Таксі").assertDoesNotExist()
    }
}
