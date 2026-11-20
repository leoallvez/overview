package br.dev.singular.overview.data.repository.catalog

import br.dev.singular.overview.data.model.CatalogDataModel
import br.dev.singular.overview.data.network.source.ICatalogRemoteDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test

class CatalogRemoteRepositoryTest {

    private val dataSource: ICatalogRemoteDataSource = mockk()
    private val sut = CatalogRemoteRepository(dataSource)

    @Test
    fun `getAll should return mapped catalogs from api`() = runTest {
        // arrange
        coEvery { dataSource.getAllFromApi() } returns listOf(CatalogDataModel(id = 1, name = "Netflix"))

        // act
        val result = sut.getAll()

        // assert
        assertEquals(1, result.size)
        assertEquals(1L, result.first().id)
        assertEquals("Netflix", result.first().name)
        coVerify(exactly = 1) { dataSource.getAllFromApi() }
    }

    @Test
    fun `getAll should return empty list when api returns empty list`() = runTest {
        // arrange
        coEvery { dataSource.getAllFromApi() } returns emptyList()

        // act
        val result = sut.getAll()

        // assert
        assertTrue(result.isEmpty())
    }
}
