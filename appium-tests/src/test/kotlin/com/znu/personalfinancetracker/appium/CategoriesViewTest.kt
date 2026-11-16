package com.znu.personalfinancetracker.appium

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class CategoriesViewTest : BaseAppiumTest() {

    @BeforeEach
    fun openCategoriesScreen() {
        click(tab("Categories"))
        waitFor(textView("CATEGORIES"))
    }

    @Test
    fun titleCategoriesAndAddButtonAreDisplayed() {
        assertDisplayed(textView("CATEGORIES"))
        CATEGORIES.forEach { assertDisplayed(textView(it)) }
        assertDisplayed(contentDesc("Add Category"))
    }

    @ParameterizedTest(name = "{index}: remove {0}")
    @ValueSource(strings = ["Зарплата", "Продукти", "Подарунки", "Інше"])
    fun removeCategoryDeletesOnlyIt(category: String) {
        click(removeButtonOf(category))
        assertNotDisplayed(textView(category))
        (CATEGORIES - category).forEach { assertDisplayed(textView(it)) }
    }
}
