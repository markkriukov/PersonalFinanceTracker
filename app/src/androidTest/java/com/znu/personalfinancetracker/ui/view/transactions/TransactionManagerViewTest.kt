package com.znu.personalfinancetracker.ui.view.transactions

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isPopup
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.znu.personalfinancetracker.data.model.category.CategoryRepositoryImpl
import com.znu.personalfinancetracker.data.model.transaction.Transaction
import com.znu.personalfinancetracker.data.model.transaction.TransactionRepositoryImpl
import com.znu.personalfinancetracker.ui.theme.PersonalFinanceTrackerTheme
import com.znu.personalfinancetracker.ui.view.categories.CategoriesViewModel
import com.znu.personalfinancetracker.ui.view.main.MainView
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TransactionManagerViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var transactionsViewModel: TransactionsViewModel
    private lateinit var categoriesViewModel: CategoriesViewModel
    private lateinit var transactionToEdit: Transaction

    @Before
    fun setUp() {
        composeTestRule.runOnUiThread {
            val categoryRepository = CategoryRepositoryImpl()
            val transactionRepository = TransactionRepositoryImpl(categoryRepository)
            transactionsViewModel = TransactionsViewModel(categoryRepository, transactionRepository)
            categoriesViewModel = CategoriesViewModel(categoryRepository)
            transactionToEdit = transactionRepository.getTransactionById("2")!!
        }
    }

    private fun showTransactionManagerView(transaction: Transaction? = null) {
        composeTestRule.setContent {
            PersonalFinanceTrackerTheme {
                TransactionManagerView(
                    viewModel = transactionsViewModel,
                    transactionToEdit = transaction
                )
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

    private fun fillForm(description: String, amount: String, date: String) {
        composeTestRule.onNodeWithText("Description").performTextInput(description)
        composeTestRule.onNodeWithText("Amount (₴)").performTextInput(amount)
        composeTestRule.onNodeWithText("Date (DD.MM.YYYY)").performTextInput(date)
    }

    private fun selectCategory(name: String) {
        composeTestRule.onNodeWithContentDescription("Toggle category").performClick()
        composeTestRule.onNode(hasText(name) and hasAnyAncestor(isPopup())).performClick()
    }

    @Test
    fun newModeShowsTitleFieldsAndDisabledSave() {
        showTransactionManagerView()

        composeTestRule.onNodeWithText("NEW TRANSACTION").assertIsDisplayed()
        composeTestRule.onNodeWithText("Description").assertIsDisplayed()
        composeTestRule.onNodeWithText("Amount (₴)").assertIsDisplayed()
        composeTestRule.onNodeWithText("Date (DD.MM.YYYY)").assertIsDisplayed()
        composeTestRule.onNodeWithText("Category").assertIsDisplayed()
        composeTestRule.onNodeWithText("Save").assertIsNotEnabled()
    }

    @Test
    fun editModeShowsTitleAndTransactionData() {
        showTransactionManagerView(transactionToEdit)

        composeTestRule.onNodeWithText("EDIT TRANSACTION").assertIsDisplayed()
        composeTestRule.onNodeWithText("Description").assertTextContains("Вода")
        composeTestRule.onNodeWithText("Amount (₴)").assertTextContains("1500.0")
        composeTestRule.onNodeWithText("Date (DD.MM.YYYY)").assertTextContains("10.02.2025")
        composeTestRule.onNodeWithText("Category").assertTextContains("Продукти")
    }

    @Test
    fun invalidAmountShowsError() {
        showTransactionManagerView()

        composeTestRule.onNodeWithText("Amount (₴)").performTextInput("abc")

        composeTestRule.onNodeWithText("Enter a valid number").assertIsDisplayed()
    }

    @Test
    fun validAmountShowsNoError() {
        showTransactionManagerView()

        composeTestRule.onNodeWithText("Amount (₴)").performTextInput("100")

        composeTestRule.onNodeWithText("Enter a valid number").assertDoesNotExist()
    }

    @Test
    fun invalidDateShowsError() {
        showTransactionManagerView()

        composeTestRule.onNodeWithText("Date (DD.MM.YYYY)").performTextInput("2025-01-01")

        composeTestRule.onNodeWithText("Format: DD.MM.YYYY").assertIsDisplayed()
    }

    @Test
    fun validDateShowsNoError() {
        showTransactionManagerView()

        composeTestRule.onNodeWithText("Date (DD.MM.YYYY)").performTextInput("01.01.2025")

        composeTestRule.onNodeWithText("Format: DD.MM.YYYY").assertDoesNotExist()
    }

    @Test
    fun categoryMenuShowsAllCategoriesAndSelectsOne() {
        showTransactionManagerView()

        composeTestRule.onNodeWithContentDescription("Toggle category").performClick()

        listOf("Зарплата", "Продукти", "Подарунки", "Інше").forEach { name ->
            composeTestRule.onNode(hasText(name) and hasAnyAncestor(isPopup())).assertIsDisplayed()
        }

        composeTestRule.onNode(hasText("Інше") and hasAnyAncestor(isPopup())).performClick()

        composeTestRule.onNode(isPopup()).assertDoesNotExist()
        composeTestRule.onNodeWithText("Category").assertTextContains("Інше")
    }

    @Test
    fun saveIsDisabledWhenDateIsEmpty() {
        showTransactionManagerView()

        composeTestRule.onNodeWithText("Description").performTextInput("Кава")
        composeTestRule.onNodeWithText("Amount (₴)").performTextInput("100")

        composeTestRule.onNodeWithText("Save").assertIsNotEnabled()
    }

    @Test
    fun saveIsDisabledWhenAmountIsInvalid() {
        showTransactionManagerView()

        fillForm("Кава", "abc", "01.03.2025")

        composeTestRule.onNodeWithText("Save").assertIsNotEnabled()
    }

    @Test
    fun saveIsEnabledWhenAllFieldsAreValid() {
        showTransactionManagerView()

        fillForm("Кава", "100", "01.03.2025")

        composeTestRule.onNodeWithText("Save").assertIsEnabled()
    }

    @Test
    fun saveAddsTransactionAndReturnsToList() {
        showMainView()
        composeTestRule.onNodeWithContentDescription("Add Transaction").performClick()

        fillForm("Кава", "100", "01.03.2025")
        composeTestRule.onNodeWithText("Save").performClick()

        composeTestRule.onNodeWithText("MY FINANCES").assertIsDisplayed()
        composeTestRule.onNodeWithText("Кава").assertIsDisplayed()
    }

    @Test
    fun newExpenseIsDisplayedWithSingleMinus() {
        showMainView()
        composeTestRule.onNodeWithContentDescription("Add Transaction").performClick()

        fillForm("Хліб", "100", "01.03.2025")
        selectCategory("Продукти")
        composeTestRule.onNodeWithText("Save").performClick()

        composeTestRule.onNode(hasText("Хліб") and hasText("-100.0 ₴")).assertIsDisplayed()
    }

    @Test
    fun saveUpdatesEditedTransaction() {
        showMainView()
        composeTestRule.onNodeWithText("Вода").performClick()

        composeTestRule.onNodeWithText("Description").performTextClearance()
        composeTestRule.onNodeWithText("Description").performTextInput("Мінеральна вода")
        composeTestRule.onNodeWithText("Save").performClick()

        composeTestRule.onNodeWithText("MY FINANCES").assertIsDisplayed()
        composeTestRule.onNodeWithText("Мінеральна вода").assertIsDisplayed()
        composeTestRule.onNodeWithText("Вода").assertDoesNotExist()
    }

    @Test
    fun cancelReturnsToListWithoutChangesAndClearsForm() {
        showMainView()
        composeTestRule.onNodeWithContentDescription("Add Transaction").performClick()

        composeTestRule.onNodeWithText("Description").performTextInput("Тест")
        composeTestRule.onNodeWithText("Cancel").performClick()

        composeTestRule.onNodeWithText("MY FINANCES").assertIsDisplayed()
        composeTestRule.onNodeWithText("Тест").assertDoesNotExist()

        composeTestRule.onNodeWithContentDescription("Add Transaction").performClick()

        composeTestRule.onNodeWithText("NEW TRANSACTION").assertIsDisplayed()
        composeTestRule.onNodeWithText("Тест").assertDoesNotExist()
    }
}
