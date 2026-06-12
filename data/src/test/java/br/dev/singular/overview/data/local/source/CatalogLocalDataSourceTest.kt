package br.dev.singular.overview.data.local.source

import br.dev.singular.overview.data.local.database.dao.CatalogDao
import br.dev.singular.overview.data.model.CatalogDataModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class CatalogLocalDataSourceTest {

    private val dao: CatalogDao = mockk(relaxed = true)
    private val sut = CatalogLocalDataSource(dao)

    @Test
    fun `insert should call dao insert`() = runTest {
        val catalogs = listOf(CatalogDataModel(id = 1))
        sut.insert(catalogs)
        coVerify { dao.insert(any()) }
    }

    @Test
    fun `getAll should return list from dao`() = runTest {
        val expected = listOf(CatalogDataModel(id = 1))
        coEvery { dao.getAll() } returns expected
        val result = sut.getAll()
        assertEquals(expected, result)
    }

    @Test
    fun `delete should call dao delete`() = runTest {
        val catalogs = listOf(CatalogDataModel(id = 1))
        sut.delete(catalogs)
        coVerify { dao.delete(any()) }
    }
}
