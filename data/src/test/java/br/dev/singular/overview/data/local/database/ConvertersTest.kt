package br.dev.singular.overview.data.local.database

import br.dev.singular.overview.data.model.MediaDataType
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Date

class ConvertersTest {

    private val sut = Converters()

    @Test
    fun `fromTimestamp should return date when value is not null`() {
        val timestamp = 1723382400000L
        val result = sut.fromTimestamp(timestamp)
        assertEquals(Date(timestamp), result)
    }

    @Test
    fun `fromTimestamp should return null when value is null`() {
        val result = sut.fromTimestamp(null)
        assertEquals(null, result)
    }

    @Test
    fun `dateToTimestamp should return timestamp when date is not null`() {
        val timestamp = 1723382400000L
        val date = Date(timestamp)
        val result = sut.dateToTimestamp(date)
        assertEquals(timestamp, result)
    }

    @Test
    fun `dateToTimestamp should return null when date is null`() {
        val result = sut.dateToTimestamp(null)
        assertEquals(null, result)
    }

    @Test
    fun `fromMediaDataType should return correct key`() {
        assertEquals("movie", sut.fromMediaDataType(MediaDataType.MOVIE))
        assertEquals("tv", sut.fromMediaDataType(MediaDataType.TV))
        assertEquals("all", sut.fromMediaDataType(MediaDataType.ALL))
        assertEquals("unknown", sut.fromMediaDataType(MediaDataType.UNKNOWN))
    }

    @Test
    fun `toMediaDataType should return correct enum`() {
        assertEquals(MediaDataType.MOVIE, sut.toMediaDataType("movie"))
        assertEquals(MediaDataType.TV, sut.toMediaDataType("tv"))
        assertEquals(MediaDataType.ALL, sut.toMediaDataType("all"))
        assertEquals(MediaDataType.UNKNOWN, sut.toMediaDataType("invalid"))
    }
}
