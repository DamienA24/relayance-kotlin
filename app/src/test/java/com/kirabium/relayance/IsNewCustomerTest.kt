package com.kirabium.relayance

import com.kirabium.relayance.domain.model.Customer
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

/**
 * Unit tests for Customer model
 * Tests the isNewCustomer method to verify business logic for identifying new customers
 */
class IsNewCustomerTest {

    @Test
    fun isNewCustomer_withCustomerCreatedToday_returnsTrue() {
        // Arrange - Customer created today
        val today = Calendar.getInstance().time
        val customer = Customer(1, "John Doe", "john@example.com", today)

        // Act
        val result = customer.isNewCustomer()

        // Assert - Should be considered a new customer
        assertTrue(result)
    }


    @Test
    fun isNewCustomer_withCustomerCreatedTwoMonthsAgo_returnsTrue() {
        // Arrange - Customer created 2 months ago
        val twoMonthsAgo = Calendar.getInstance()
        twoMonthsAgo.add(Calendar.MONTH, -2)
        val customer = Customer(3, "Bob Wilson", "bob@example.com", twoMonthsAgo.time)

        // Act
        val result = customer.isNewCustomer()

        // Assert - Should still be considered a new customer (within 3 months)
        assertTrue(result)
    }

    @Test
    fun isNewCustomer_withCustomerAtThreeMonthBoundary_returnsCorrectValue() {
        // Arrange - Customer created almost exactly 3 months ago
        val almostThreeMonths = Calendar.getInstance()
        almostThreeMonths.add(Calendar.MONTH, -3)
        almostThreeMonths.add(Calendar.DAY_OF_MONTH, 1)
        val customer = Customer(7, "Frank Miller", "frank@example.com", almostThreeMonths.time)

        // Act
        val result = customer.isNewCustomer()

        // Assert - Should still be considered new (just under 3 months)
        assertTrue(result)
    }


    @Test
    fun isNewCustomer_withCustomerCreatedSixMonthsAgo_returnsFalse() {
        // Arrange - Customer created 6 months ago
        val sixMonthsAgo = Calendar.getInstance()
        sixMonthsAgo.add(Calendar.MONTH, -6)
        val customer = Customer(5, "Charlie Davis", "charlie@example.com", sixMonthsAgo.time)

        // Act
        val result = customer.isNewCustomer()

        // Assert - Should NOT be considered a new customer (more than 3 months)
        assertFalse(result)
    }

}
