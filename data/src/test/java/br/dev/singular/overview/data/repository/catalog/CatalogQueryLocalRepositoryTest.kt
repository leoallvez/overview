package br.dev.singular.overview.data.repository.catalog

import br.dev.singular.overview.data.local.source.DataStoreDataSource
import br.dev.singular.overview.data.model.QueryDataState
import br.dev.singular.overview.domain.model.QueryState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test

class CatalogQueryLocalRepositoryTest {

    @MockK(relaxed = true)
    private lateinit var dataSource: DataStoreDataSource

    private val json = Json { ignoreUnknownKeys = true }

    private lateinit var sut: CatalogQueryLocalRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        sut = CatalogQueryLocalRepository(json, dataSource)
    }

    @Test
    fun `get should return QueryState when json is valid`() = runTest {
        // Arrange
        val path = "trending/all/day"
        val queryDataState = QueryDataState(path = path)
        val jsonString = json.encodeToString(queryDataState)
        coEvery { dataSource.getValue(any<androidx.datastore.preferences.core.Preferences.Key<String>>()) } returns flowOf(
            jsonString
        )

        // Act
        val result = sut.get()

        // Assert
        assertEquals(path, result?.key)
    }

    @Test
    fun `get should return null when json is missing`() = runTest {
        // Arrange
        coEvery { dataSource.getValue(any<androidx.datastore.preferences.core.Preferences.Key<String>>()) } returns flowOf(
            null
        )

        // Act
        val result = sut.get()

        // Assert
        assertNull(result)
    }

    @Test
    fun `update should call data source setValue with json string`() = runTest {
        // Arrange
        val queryState = QueryState(key = "new_key")

        // Act
        sut.update(queryState)

        // Assert
        coVerify { dataSource.setValue(any(), any<String>()) }
    }
}
