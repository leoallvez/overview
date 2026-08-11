package br.dev.singular.overview.data.local.source

import br.dev.singular.overview.data.local.database.dao.CatalogDao
import br.dev.singular.overview.data.util.fakeCatalogDataModel
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
    fun `insert should call dao insert with correct arguments`() = runTest {
        val catalogs = listOf(fakeCatalogDataModel)
        sut.insert(catalogs)
        coVerify { dao.insert(fakeCatalogDataModel) }
    }

    @Test
    fun `getAll should return list from dao`() = runTest {
        val expected = listOf(fakeCatalogDataModel)
        coEvery { dao.getAll() } returns expected
        val result = sut.getAll()
        assertEquals(expected, result)
    }

    @Test
    fun `delete should call dao delete with correct arguments`() = runTest {
        val catalogs = listOf(fakeCatalogDataModel)
        sut.delete(catalogs)
        coVerify { dao.delete(fakeCatalogDataModel) }
    }
}
