package com.kirabium.relayance

import com.kirabium.relayance.extension.DateExt.Companion.toHumanDate
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar

/**
 * Unit tests for DateExt extension functions
 * Tests the toHumanDate method to ensure date formatting is correct
 */
class ToHumanDateTest {

    @Test
    fun toHumanDate_withSpecificDate_returnsCorrectFormat() {
        // Arrange - Create a specific date: January 15, 2024
        val calendar = Calendar.getInstance()
        calendar.set(2024, Calendar.JANUARY, 15, 0, 0, 0)
        val date = calendar.time

        // Act
        val result = date.toHumanDate()

        // Assert - Should return in dd/MM/yyyy format
        assertEquals("15/01/2024", result)
    }

    @Test
    fun toHumanDate_withFirstDayOfMonth_returnsCorrectFormat() {
        // Arrange - Create date: March 1, 2023
        val calendar = Calendar.getInstance()
        calendar.set(2023, Calendar.MARCH, 1, 0, 0, 0)
        val date = calendar.time

        // Act
        val result = date.toHumanDate()

        // Assert
        assertEquals("01/03/2023", result)
    }

    @Test
    fun toHumanDate_withLastDayOfMonth_returnsCorrectFormat() {
        // Arrange - Create date: December 31, 2023
        val calendar = Calendar.getInstance()
        calendar.set(2023, Calendar.DECEMBER, 31, 0, 0, 0)
        val date = calendar.time

        // Act
        val result = date.toHumanDate()

        // Assert
        assertEquals("31/12/2023", result)
    }

}
