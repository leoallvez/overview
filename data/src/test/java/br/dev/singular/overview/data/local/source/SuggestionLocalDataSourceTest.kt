package br.dev.singular.overview.data.local.source

import br.dev.singular.overview.data.local.database.dao.SuggestionDao
import br.dev.singular.overview.data.util.fakeSuggestionModels
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SuggestionLocalDataSourceTest {

    private val dao: SuggestionDao = mockk(relaxed = true)
    private val sut = SuggestionLocalDataSource(dao)

    @Test
    fun `insert should call dao insert with correct arguments`() = runTest {
        val suggestions = fakeSuggestionModels
        sut.insert(suggestions)
        coVerify { dao.insert(*suggestions.toTypedArray()) }
    }

    @Test
    fun `getAll should return list from dao`() = runTest {
        val expected = fakeSuggestionModels
        coEvery { dao.getAll() } returns expected
        val result = sut.getAll()
        assertEquals(expected, result)
    }

    @Test
    fun `delete should call dao delete with correct arguments`() = runTest {
        val suggestions = fakeSuggestionModels
        sut.delete(suggestions)
        coVerify { dao.delete(*suggestions.toTypedArray()) }
    }
}
