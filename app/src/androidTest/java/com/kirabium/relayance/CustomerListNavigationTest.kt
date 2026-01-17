package com.kirabium.relayance

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.recyclerview.widget.RecyclerView
import com.kirabium.relayance.ui.activity.DetailActivity
import com.kirabium.relayance.ui.activity.MainActivity
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Integration test for navigation from customer list to detail screen.
 * Uses Espresso-Intents to verify that clicking on a customer launches
 * the correct Intent with the proper customer ID.
 */
@RunWith(AndroidJUnit4::class)
class CustomerListNavigationTest {

    // Rule to launch MainActivity before each test
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        // Initialize Espresso-Intents before each test
        Intents.init()
    }

    @After
    fun tearDown() {
        // Release Espresso-Intents after each test
        Intents.release()
    }

    /**
     * Test: Verify that clicking on the first customer launches DetailActivity with correct ID.
     *
     * This test simulates a click on the first item in the RecyclerView (Alice Wonderland, ID=1)
     * and verifies that:
     * 1. The Intent targets DetailActivity
     * 2. The Intent contains the correct customer ID (1) as an extra
     */
    @Test
    fun clickOnFirstCustomer_launchesDetailActivityWithCorrectId() {
        // Act: Click on the first item in the RecyclerView
        onView(withId(R.id.customerRecyclerView))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        // Assert: Verify the Intent was launched with correct component and extra
        intended(
            allOf(
                hasComponent(DetailActivity::class.java.name),
                hasExtra(DetailActivity.EXTRA_CUSTOMER_ID, 1) // Alice Wonderland's ID
            )
        )
    }
}
