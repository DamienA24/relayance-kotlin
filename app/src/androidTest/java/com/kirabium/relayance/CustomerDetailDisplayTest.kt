package com.kirabium.relayance

import android.content.Intent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.kirabium.relayance.ui.activity.DetailActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Integration test for the customer detail screen using Compose Testing.
 * Verifies that customer information is correctly displayed when launching
 * DetailActivity with a specific customer ID.
 */
@RunWith(AndroidJUnit4::class)
class CustomerDetailDisplayTest {

    // Compose test rule for assertions on Compose UI
    @get:Rule
    val composeTestRule = createEmptyComposeRule()

    /**
     * Test: Verify that Alice Wonderland's details are correctly displayed.
     *
     * This test launches DetailActivity with Alice's ID (1) and verifies:
     * 1. Her name "Alice Wonderland" is displayed
     * 2. Her email "alice@example.com" is displayed
     * 3. The "New" badge is NOT displayed (she was created 12 months ago)
     */
    @Test
    fun detailScreen_withAliceId_displaysCorrectInformation() {
        // Arrange: Create an Intent with Alice's customer ID (1)
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(context, DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_CUSTOMER_ID, 1) // Alice Wonderland's ID
        }

        // Launch DetailActivity with the configured Intent
        ActivityScenario.launch<DetailActivity>(intent).use {
            // Assert: Verify Alice's name is displayed
            composeTestRule
                .onNodeWithText("Alice Wonderland")
                .assertIsDisplayed()

            // Assert: Verify Alice's email is displayed
            composeTestRule
                .onNodeWithText("alice@example.com")
                .assertIsDisplayed()

            // Assert: Verify the "New" badge is NOT displayed
            // Alice was created 12 months ago, so she is not a new customer
            composeTestRule
                .onNodeWithText("New")
                .assertDoesNotExist()
        }
    }
}
