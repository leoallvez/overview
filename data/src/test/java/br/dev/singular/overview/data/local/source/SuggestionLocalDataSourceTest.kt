package br.dev.singular.overview.data.local.source

import br.dev.singular.overview.data.local.database.dao.SuggestionDao
import br.dev.singular.overview.data.model.SuggestionDataModel
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
    fun `insert should call dao insert`() = runTest {
        val suggestions = listOf(SuggestionDataModel(id = 1))
        sut.insert(suggestions)
        coVerify { dao.insert(any()) }
    }

    @Test
    fun `getAll should return list from dao`() = runTest {
        val expected = listOf(SuggestionDataModel(id = 1))
        coEvery { dao.getAll() } returns expected
        val result = sut.getAll()
        assertEquals(expected, result)
    }

    @Test
    fun `delete should call dao delete`() = runTest {
        val suggestions = listOf(SuggestionDataModel(id = 1))
        sut.delete(suggestions)
        coVerify { dao.delete(any()) }
    }
}
