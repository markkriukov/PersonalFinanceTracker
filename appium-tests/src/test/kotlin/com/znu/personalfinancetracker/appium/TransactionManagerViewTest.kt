package com.znu.personalfinancetracker.appium

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class TransactionManagerViewTest : BaseAppiumTest() {

    @Test
    fun newModeShowsTitleFieldsAndDisabledSave() {
        openNewTransaction()
        assertDisplayed(textView("NEW TRANSACTION"))
        listOf("Description", "Amount (₴)", "Date (DD.MM.YYYY)", "Category").forEach {
            assertDisplayed(inputField(it))
        }
        assertEnabled(button("Save"), false)
    }

    @Test
    fun editModeShowsTitleAndTransactionData() {
        click(textView("Вода"))
        assertDisplayed(textView("EDIT TRANSACTION"))
        assertFieldValue("Description", "Вода")
        assertFieldValue("Amount (₴)", "1500.0")
        assertFieldValue("Date (DD.MM.YYYY)", "10.02.2025")
        assertFieldValue("Category", "Продукти")
    }

    @ParameterizedTest(name = "{index}: amount = {0} -> error {1}")
    @CsvSource(
        "abc, true",
        "12a, true",
        "'1,5', true",
        "100, false",
        "99.5, false",
        "0, false"
    )
    fun amountValidation(amount: String, isErrorExpected: Boolean) {
        openNewTransaction()
        typeInto("Amount (₴)", amount)
        assertShown(textView("Enter a valid number"), isErrorExpected)
    }

    @ParameterizedTest(name = "{index}: date = {0} -> error {1}")
    @CsvSource(
        "2025-01-01, true",
        "1.1.2025, true",
        "abc, true",
        "01.01.2025, false",
        "15.02.2025, false"
    )
    fun dateValidation(date: String, isErrorExpected: Boolean) {
        openNewTransaction()
        typeInto("Date (DD.MM.YYYY)", date)
        assertShown(textView("Format: DD.MM.YYYY"), isErrorExpected)
    }

    @Test
    fun categoryMenuShowsAllCategoriesAndSelectsOne() {
        openNewTransaction()
        click(contentDesc("Toggle category"))
        CATEGORIES.forEach { assertDisplayed(textView(it)) }
        click(textView("Інше"))
        assertNotDisplayed(textView("Продукти"))
        assertFieldValue("Category", "Інше")
    }

    @ParameterizedTest(name = "{index}: [{0}] [{1}] [{2}] -> Save enabled {3}")
    @CsvSource(
        "'', 100, 01.03.2025, false",
        "Кава, '', 01.03.2025, false",
        "Кава, 100, '', false",
        "Кава, abc, 01.03.2025, false",
        "Кава, 100, 2025-03-01, false",
        "Кава, 100, 01.03.2025, true"
    )
    fun saveButtonState(description: String, amount: String, date: String, isSaveEnabled: Boolean) {
        openNewTransaction()
        if (description.isNotEmpty()) typeInto("Description", description)
        if (amount.isNotEmpty()) typeInto("Amount (₴)", amount)
        if (date.isNotEmpty()) typeInto("Date (DD.MM.YYYY)", date)
        assertEnabled(button("Save"), isSaveEnabled)
    }

    @Test
    fun saveAddsTransactionAndReturnsToList() {
        openNewTransaction()
        fillTransactionForm("Кава", "100", "01.03.2025")
        click(button("Save"))
        assertDisplayed(textView("MY FINANCES"))
        assertDisplayed(textView("Кава"))
    }

    @Test
    fun newExpenseIsDisplayedWithSingleMinus() {
        openNewTransaction()
        fillTransactionForm("Хліб", "100", "01.03.2025")
        selectCategory("Продукти")
        click(button("Save"))
        assertDisplayed(transactionRow("Хліб", "-100.0 ₴"))
    }

    @Test
    fun saveUpdatesEditedTransaction() {
        openTransaction("Вода")
        typeInto("Description", "Мінеральна вода")
        click(button("Save"))
        assertDisplayed(textView("MY FINANCES"))
        assertDisplayed(textView("Мінеральна вода"))
        assertNotDisplayed(textView("Вода"))
    }

    @Test
    fun cancelReturnsToListWithoutChangesAndClearsForm() {
        openNewTransaction()
        typeInto("Description", "Тест")
        click(button("Cancel"))
        assertDisplayed(textView("MY FINANCES"))
        assertNotDisplayed(textView("Тест"))
        openNewTransaction()
        assertFieldValue("Description", "")
    }
}
