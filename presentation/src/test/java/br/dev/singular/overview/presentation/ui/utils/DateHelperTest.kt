package br.dev.singular.overview.presentation.ui.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar
import java.util.Locale

class DateHelperTest {

    @Test
    fun `formattedDate should return formatted date for Latin languages`() {
        Locale.setDefault(Locale("pt", "BR"))
        val dateIn = "2023-10-27"
        val helper = DateHelper(dateIn)
        assertEquals("27/10/2023", helper.formattedDate())
    }

    @Test
    fun `formattedDate should return formatted date for Non-Latin languages`() {
        Locale.setDefault(Locale.US)
        val dateIn = "2023-10-27"
        val helper = DateHelper(dateIn)
        assertEquals("10/27/2023", helper.formattedDate())
    }

    @Test
    fun `formattedDate should return empty string for null input`() {
        val helper = DateHelper(null)
        assertEquals("", helper.formattedDate())
    }

    @Test
    fun `periodBetween should calculate period correctly`() {
        val birthDate = "1990-01-01"
        val helper = DateHelper(birthDate)
        
        val result = helper.periodBetween("2023-01-01")
        assertEquals("33", result)
    }

    @Test
    fun `periodBetween should return empty string for invalid dates`() {
        val helper = DateHelper("invalid-date")
        assertEquals("", helper.periodBetween("2023-01-01"))
    }

    @Test
    fun `isFutureDate should return true for future dates`() {
        val tomorrow = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 1)
        }
        val tomorrowStr = "${tomorrow.get(Calendar.YEAR)}-${tomorrow.get(Calendar.MONTH) + 1}-${tomorrow.get(Calendar.DAY_OF_MONTH)}"
        
        val helper = DateHelper(tomorrowStr)
        assertTrue(helper.isFutureDate())
    }

    @Test
    fun `isFutureDate should return false for past dates`() {
        val helper = DateHelper("2000-01-01")
        assertFalse(helper.isFutureDate())
    }
}
