package br.dev.singular.overview.data.repository.catalog

import br.dev.singular.overview.data.local.source.ICatalogLocalDataSource
import br.dev.singular.overview.data.model.CatalogDataModel
import br.dev.singular.overview.data.network.source.ICatalogRemoteDataSource
import br.dev.singular.overview.domain.model.Catalog
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.util.Date

class CatalogRepositoryTest {

    @MockK(relaxed = true)
    private lateinit var localSource: ICatalogLocalDataSource

    @MockK(relaxed = true)
    private lateinit var remoteSource: ICatalogRemoteDataSource

    private lateinit var sut: CatalogRepository

    private val catalogData = CatalogDataModel(id = 1, name = "Netflix")

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        sut = CatalogRepository(localSource, remoteSource)
    }

    @Test
    fun `getAll should return data from local source when it is not empty`() = runTest {
        // Arrange
        coEvery { localSource.getAll() } returns listOf(catalogData)

        // Act
        val result = sut.getAll()

        // Assert
        assertEquals(1, result.size)
        assertEquals("Netflix", result.first().name)
        coVerify(exactly = 0) { remoteSource.getAll() }
    }

    @Test
    fun `getAll should fetch from remote and save to local when local is empty`() = runTest {
        // Arrange
        coEvery { localSource.getAll() } returns emptyList()
        coEvery { remoteSource.getAll() } returns listOf(catalogData)

        // Act
        val result = sut.getAll()

        // Assert
        assertEquals(1, result.size)
        coVerify {
            remoteSource.getAll()
            localSource.insert(listOf(catalogData))
        }
    }

    @Test
    fun `delete should call local source delete`() = runTest {
        // Arrange
        val catalog = Catalog(
            id = 1,
            name = "Netflix",
            priority = 1,
            logoPath = "",
            display = true,
            lastUpdate = Date()
        )

        // Act
        sut.delete(catalog)

        // Assert
        coVerify { localSource.delete(any()) }
    }
}
