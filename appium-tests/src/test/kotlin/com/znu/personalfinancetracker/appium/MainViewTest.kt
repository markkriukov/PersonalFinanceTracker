package com.znu.personalfinancetracker.appium

import org.junit.jupiter.api.Test

class MainViewTest : BaseAppiumTest() {

    @Test
    fun launchShowsAppTitleTabsAndTransactionsScreen() {
        assertDisplayed(textView("Personal Finance Tracker"))
        assertDisplayed(textView("Transactions"))
        assertSelected(tab("Transactions"))
        assertDisplayed(textView("Categories"))
        assertDisplayed(textView("MY FINANCES"))
    }

    @Test
    fun categoriesTabOpensCategoriesScreen() {
        click(tab("Categories"))
        assertSelected(tab("Categories"))
        assertDisplayed(textView("CATEGORIES"))
    }

    @Test
    fun transactionsTabReturnsToTransactionsScreen() {
        click(tab("Categories"))
        waitFor(textView("CATEGORIES"))
        click(tab("Transactions"))
        assertSelected(tab("Transactions"))
        assertDisplayed(textView("MY FINANCES"))
    }
}
