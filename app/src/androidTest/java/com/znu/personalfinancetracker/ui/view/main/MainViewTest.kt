package com.znu.personalfinancetracker.ui.view.main

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.znu.personalfinancetracker.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainViewTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun launchShowsAppTitleTabsAndTransactionsScreen() {
        composeTestRule.onNodeWithText("Personal Finance Tracker").assertIsDisplayed()
        composeTestRule.onNodeWithText("Transactions").assertIsDisplayed().assertIsSelected()
        composeTestRule.onNodeWithText("Categories").assertIsDisplayed()
        composeTestRule.onNodeWithText("MY FINANCES").assertIsDisplayed()
    }

    @Test
    fun categoriesTabOpensCategoriesScreen() {
        composeTestRule.onNodeWithText("Categories").performClick()

        composeTestRule.onNodeWithText("Categories").assertIsSelected()
        composeTestRule.onNodeWithText("CATEGORIES").assertIsDisplayed()
    }

    @Test
    fun transactionsTabReturnsToTransactionsScreen() {
        composeTestRule.onNodeWithText("Categories").performClick()
        composeTestRule.onNodeWithText("Transactions").performClick()

        composeTestRule.onNodeWithText("Transactions").assertIsSelected()
        composeTestRule.onNodeWithText("MY FINANCES").assertIsDisplayed()
    }
}
