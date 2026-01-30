package com.kirabium.relayance.cucumber

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.kirabium.relayance.R
import com.kirabium.relayance.data.DummyData
import com.kirabium.relayance.ui.activity.AddCustomerActivity
import com.kirabium.relayance.ui.activity.MainActivity
import io.cucumber.java.After
import io.cucumber.java.Before
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

class AddCustomerSteps {

    private lateinit var addCustomerScenario: ActivityScenario<AddCustomerActivity>
    private lateinit var mainActivityScenario: ActivityScenario<MainActivity>
    private var initialCustomerCount = 0

    @Before
    fun setUp() {
        initialCustomerCount = DummyData.customers.size
    }

    @After
    fun tearDown() {
        if (::addCustomerScenario.isInitialized) {
            addCustomerScenario.close()
        }
        if (::mainActivityScenario.isInitialized) {
            mainActivityScenario.close()
        }
    }

    @Given("the user is on the add customer screen")
    fun theUserIsOnTheAddCustomerScreen() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), AddCustomerActivity::class.java)
        addCustomerScenario = ActivityScenario.launch(intent)
        Thread.sleep(500)
    }

    @When("the user enters the name {string}")
    fun theUserEntersTheName(name: String) {
        onView(withId(R.id.nameEditText))
            .perform(typeText(name), closeSoftKeyboard())
        Thread.sleep(300)
    }

    @And("the user enters the email {string}")
    fun theUserEntersTheEmail(email: String) {
        onView(withId(R.id.emailEditText))
            .perform(typeText(email), closeSoftKeyboard())
        Thread.sleep(300)
    }

    @And("the user clicks the add button")
    fun theUserClicksTheAddButton() {
        onView(withId(R.id.saveFab))
            .perform(click())
        Thread.sleep(1500)
    }

    @Then("a success confirmation is displayed")
    fun aSuccessConfirmationIsDisplayed() {
        assertEquals(initialCustomerCount + 1, DummyData.customers.size)
    }

    @And("the customer {string} appears in the customer list")
    fun theCustomerAppearsInTheCustomerList(customerName: String) {
        Thread.sleep(500)
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        mainActivityScenario = ActivityScenario.launch(intent)
        Thread.sleep(1000)

        onView(withText(customerName))
            .check(matches(isDisplayed()))
    }

    @Then("an error message is displayed")
    fun anErrorMessageIsDisplayed() {
        Thread.sleep(500)
        assertEquals(initialCustomerCount, DummyData.customers.size)
    }

    @And("the customer is not added to the list")
    fun theCustomerIsNotAddedToTheList() {
        assertEquals(initialCustomerCount, DummyData.customers.size)
    }
}
