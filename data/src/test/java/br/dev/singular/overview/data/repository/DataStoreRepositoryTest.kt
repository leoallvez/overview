package br.dev.singular.overview.data.repository

import androidx.datastore.preferences.core.stringPreferencesKey
import br.dev.singular.overview.data.local.source.DataStoreDataSource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DataStoreRepositoryTest {

    @MockK(relaxed = true)
    private lateinit var dataSource: DataStoreDataSource

    private val key = stringPreferencesKey("test_key")
    private val defaultValue = "default"

    private lateinit var sut: DataStoreRepository<String>

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        sut = DataStoreRepository(key, dataSource, defaultValue)
    }

    @Test
    fun `get should return value from data source when it exists`() = runTest {
        // Arrange
        coEvery { dataSource.getValue(key) } returns flowOf("stored_value")

        // Act
        val result = sut.get()

        // Assert
        assertEquals("stored_value", result)
    }

    @Test
    fun `get should return default value when data source returns null`() = runTest {
        // Arrange
        coEvery { dataSource.getValue(key) } returns flowOf(null)

        // Act
        val result = sut.get()

        // Assert
        assertEquals(defaultValue, result)
    }

    @Test
    fun `update should call data source setValue`() = runTest {
        // Act
        sut.update("new_value")

        // Assert
        coVerify { dataSource.setValue(key, "new_value") }
    }
}
