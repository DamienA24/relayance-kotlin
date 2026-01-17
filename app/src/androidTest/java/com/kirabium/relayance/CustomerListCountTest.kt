package com.kirabium.relayance

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kirabium.relayance.ui.activity.MainActivity
import com.kirabium.relayance.util.RecyclerViewItemCountAssertion
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Integration test for the customer list screen.
 * Verifies that the RecyclerView displays the correct number of customers at app launch.
 */
@RunWith(AndroidJUnit4::class)
class CustomerListCountTest {

    // Rule to launch MainActivity before each test
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    /**
     * Test: Verify that the customer list displays exactly 5 items at startup.
     *
     * This test checks that when the app launches, the RecyclerView
     * contains all 5 pre-coded customers from DummyData.
     */
    @Test
    fun customerList_atStartup_displaysFiveItems() {
        // Act & Assert: Check that RecyclerView has exactly 5 items
        onView(withId(R.id.customerRecyclerView))
            .check(RecyclerViewItemCountAssertion.withItemCount(5))
    }
}
