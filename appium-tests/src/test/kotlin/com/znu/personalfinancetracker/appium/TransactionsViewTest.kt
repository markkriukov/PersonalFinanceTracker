package com.znu.personalfinancetracker.appium

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class TransactionsViewTest : BaseAppiumTest() {

    @Test
    fun titleAndInitialTransactionsAreDisplayed() {
        assertDisplayed(textView("MY FINANCES"))
        listOf("ФОП", "Вода", "Квіти", "Шоколад").forEach { assertDisplayed(textView(it)) }
        assertDisplayed(textView("01.02.2025"))
        assertDisplayed(textView("10.02.2025"))
        assertEquals(2, driver.findElements(textView("15.02.2025")).size)
    }

    @ParameterizedTest(name = "{index}: {0} -> {1}")
    @CsvSource(
        "ФОП, +25000.0 ₴",
        "Вода, -1500.0 ₴",
        "Квіти, -200.0 ₴",
        "Шоколад, -100.0 ₴"
    )
    fun transactionShowsSignedAmount(description: String, expectedAmount: String) {
        assertDisplayed(transactionRow(description, expectedAmount))
    }

    @Test
    fun addTransactionButtonOpensNewTransactionScreen() {
        click(contentDesc("Add Transaction"))
        assertDisplayed(textView("NEW TRANSACTION"))
    }

    @Test
    fun transactionClickOpensEditTransactionScreen() {
        click(textView("Вода"))
        assertDisplayed(textView("EDIT TRANSACTION"))
        assertFieldValue("Description", "Вода")
    }
}
