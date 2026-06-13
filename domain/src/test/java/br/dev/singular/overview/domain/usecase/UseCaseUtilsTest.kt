package br.dev.singular.overview.domain.usecase

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

class UseCaseUtilsTest {

    @Test
    fun `runSafely should return Success when action succeeds`() = runTest {
        val expected = "Result"
        val result = runSafely { expected }

        assertEquals(UseCaseState.Success(expected), result)
    }

    @Test
    fun `runSafely should return Failure when action throws exception`() = runTest {
        val exception = RuntimeException("Error")
        val result = runSafely { throw exception }

        assertTrue(result is UseCaseState.Failure)
        assertEquals(exception, ((result as UseCaseState.Failure).type as FailType.Exception).throwable)
    }

    @Test
    fun `adjustDate should add days correctly`() {
        val calendar = Calendar.getInstance().apply {
            set(2023, Calendar.JANUARY, 1)
        }
        val date = calendar.time

        val result = date.adjustDate(5)

        val resultCalendar = Calendar.getInstance().apply { time = result }
        assertEquals(2023, resultCalendar.get(Calendar.YEAR))
        assertEquals(Calendar.JANUARY, resultCalendar.get(Calendar.MONTH))
        assertEquals(6, resultCalendar.get(Calendar.DAY_OF_MONTH))
    }

    @Test
    fun `runSafely should return Success with null when action returns null`() = runTest {
        val result = runSafely<String?> { null }

        assertEquals(UseCaseState.Success(null), result)
    }

    @Test
    fun `adjustDate should handle month overflow`() {
        val calendar = Calendar.getInstance().apply {
            set(2023, Calendar.JANUARY, 31)
        }
        val date = calendar.time

        val result = date.adjustDate(1)

        val resultCalendar = Calendar.getInstance().apply { time = result }
        assertEquals(2023, resultCalendar.get(Calendar.YEAR))
        assertEquals(Calendar.FEBRUARY, resultCalendar.get(Calendar.MONTH))
        assertEquals(1, resultCalendar.get(Calendar.DAY_OF_MONTH))
    }

    @Test
    fun `adjustDate should handle year overflow`() {
        val calendar = Calendar.getInstance().apply {
            set(2023, Calendar.DECEMBER, 31)
        }
        val date = calendar.time

        val result = date.adjustDate(1)

        val resultCalendar = Calendar.getInstance().apply { time = result }
        assertEquals(2024, resultCalendar.get(Calendar.YEAR))
        assertEquals(Calendar.JANUARY, resultCalendar.get(Calendar.MONTH))
        assertEquals(1, resultCalendar.get(Calendar.DAY_OF_MONTH))
    }
}
