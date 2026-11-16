package com.znu.personalfinancetracker.appium

import io.appium.java_client.AppiumBy
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.android.options.UiAutomator2Options
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.io.File
import java.net.URI
import java.time.Duration

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class BaseAppiumTest {

    protected lateinit var driver: AndroidDriver
    private lateinit var wait: WebDriverWait

    @BeforeAll
    fun startSession() {
        val options = UiAutomator2Options()
            .setApp(APP_PATH)
            .setAppPackage(APP_PACKAGE)
            .setAppActivity(APP_ACTIVITY)
            .setNewCommandTimeout(Duration.ofMinutes(2))
        driver = AndroidDriver(URI(SERVER_URL).toURL(), options)
        wait = WebDriverWait(driver, Duration.ofSeconds(10))
    }

    @BeforeEach
    fun restartApp() {
        driver.terminateApp(APP_PACKAGE)
        driver.activateApp(APP_PACKAGE)
        WebDriverWait(driver, APP_START_TIMEOUT)
            .until(ExpectedConditions.visibilityOfElementLocated(textView("MY FINANCES")))
    }

    @AfterAll
    fun stopSession() {
        if (::driver.isInitialized) driver.quit()
    }

    protected fun textView(text: String): By =
        By.xpath("//android.widget.TextView[@text='$text']")

    protected fun contentDesc(description: String): By = AppiumBy.accessibilityId(description)

    protected fun inputField(label: String): By =
        By.xpath("//android.widget.EditText[android.widget.TextView[@text='$label']]")

    protected fun button(label: String): By =
        By.xpath("//android.widget.TextView[@text='$label']/..")

    protected fun tab(title: String): By = button(title)

    protected fun transactionRow(description: String, amount: String): By =
        By.xpath(
            "//*[android.widget.TextView[@text='$description']" +
                " and android.widget.TextView[@text='$amount']]"
        )

    protected fun removeButtonOf(category: String): By =
        By.xpath(
            "//*[android.widget.TextView[@text='$category']]" +
                "//*[@content-desc='Remove category']"
        )

    protected fun waitFor(locator: By): WebElement =
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator))

    protected fun click(locator: By) = waitFor(locator).click()

    protected fun assertDisplayed(locator: By) = assertTrue(waitFor(locator).isDisplayed)

    protected fun assertNotDisplayed(locator: By) =
        assertTrue(wait.until(ExpectedConditions.invisibilityOfElementLocated(locator)))

    protected fun assertShown(locator: By, isShown: Boolean) =
        if (isShown) assertDisplayed(locator) else assertNotDisplayed(locator)

    protected fun assertSelected(locator: By) =
        assertTrue(wait.until(ExpectedConditions.attributeToBe(locator, "selected", "true")))

    protected fun assertEnabled(locator: By, isEnabled: Boolean) =
        assertTrue(
            wait.until(ExpectedConditions.attributeToBe(locator, "enabled", isEnabled.toString()))
        )

    protected fun assertFieldValue(label: String, value: String) =
        assertTrue(wait.until(ExpectedConditions.textToBe(inputField(label), value)))

    protected fun typeInto(label: String, value: String) {
        val input = waitFor(inputField(label))
        input.clear()
        wait.until(ExpectedConditions.textToBe(inputField(label), ""))
        input.sendKeys(value)
        wait.until(ExpectedConditions.textToBe(inputField(label), value))
        hideKeyboard()
    }

    protected fun hideKeyboard() {
        if (driver.isKeyboardShown) driver.hideKeyboard()
    }

    protected fun openNewTransaction() {
        click(contentDesc("Add Transaction"))
        waitFor(textView("NEW TRANSACTION"))
    }

    protected fun openTransaction(description: String) {
        click(textView(description))
        waitFor(textView("EDIT TRANSACTION"))
        assertFieldValue("Description", description)
    }

    protected fun fillTransactionForm(description: String, amount: String, date: String) {
        typeInto("Description", description)
        typeInto("Amount (₴)", amount)
        typeInto("Date (DD.MM.YYYY)", date)
    }

    protected fun selectCategory(category: String) {
        click(contentDesc("Toggle category"))
        click(textView(category))
        assertFieldValue("Category", category)
    }

    companion object {
        const val SERVER_URL = "http://127.0.0.1:4723"
        const val APP_PACKAGE = "com.znu.personalfinancetracker"
        const val APP_ACTIVITY = ".MainActivity"
        val APP_START_TIMEOUT: Duration = Duration.ofSeconds(30)
        val APP_PATH: String = File("../app/build/outputs/apk/debug/app-debug.apk").canonicalPath
        val CATEGORIES = listOf("Зарплата", "Продукти", "Подарунки", "Інше")
    }
}
