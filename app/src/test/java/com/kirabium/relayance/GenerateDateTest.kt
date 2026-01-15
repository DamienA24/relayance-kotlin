package com.kirabium.relayance

import com.kirabium.relayance.data.DummyData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

/**
 * Unit tests for DummyData class
 * Tests the generateDate method to ensure dates are calculated correctly
 */
class GenerateDateTest {

    @Test
    fun generateDate_withZeroMonths_returnsCurrentMonth() {
        // Arrange
        val expectedCalendar = Calendar.getInstance()

        // Act
        val result = DummyData.generateDate(0)
        val resultCalendar = Calendar.getInstance()
        resultCalendar.time = result

        // Assert - Check year and month are the same
        assertEquals(expectedCalendar.get(Calendar.YEAR), resultCalendar.get(Calendar.YEAR))
        assertEquals(expectedCalendar.get(Calendar.MONTH), resultCalendar.get(Calendar.MONTH))
    }

    @Test
    fun generateDate_withOneMonth_returnsOneMonthAgo() {
        // Arrange
        val expectedCalendar = Calendar.getInstance()
        expectedCalendar.add(Calendar.MONTH, -1)

        // Act
        val result = DummyData.generateDate(1)
        val resultCalendar = Calendar.getInstance()
        resultCalendar.time = result

        // Assert - Check year and month are correct
        assertEquals(expectedCalendar.get(Calendar.YEAR), resultCalendar.get(Calendar.YEAR))
        assertEquals(expectedCalendar.get(Calendar.MONTH), resultCalendar.get(Calendar.MONTH))
    }

    @Test
    fun generateDate_withTwelveMonths_returnsOneYearAgo() {
        // Arrange
        val expectedCalendar = Calendar.getInstance()
        expectedCalendar.add(Calendar.MONTH, -12)

        // Act
        val result = DummyData.generateDate(12)
        val resultCalendar = Calendar.getInstance()
        resultCalendar.time = result

        // Assert - Check year and month are correct
        assertEquals(expectedCalendar.get(Calendar.YEAR), resultCalendar.get(Calendar.YEAR))
        assertEquals(expectedCalendar.get(Calendar.MONTH), resultCalendar.get(Calendar.MONTH))
    }

    @Test
    fun generateDate_withSixMonths_returnsDateInThePast() {
        // Arrange
        val now = Calendar.getInstance()

        // Act
        val result = DummyData.generateDate(6)

        // Assert - Generated date should be before now
        assertTrue(result.before(now.time))
    }
}
