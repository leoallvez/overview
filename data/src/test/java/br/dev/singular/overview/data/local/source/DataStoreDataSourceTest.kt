package br.dev.singular.overview.data.local.source

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class DataStoreDataSourceTest {

    private val dataStore: DataStore<Preferences> = mockk(relaxed = true)
    private val sut = DataStoreDataSource(dataStore)

    @Test
    fun `setValue should call edit on datastore`() = runTest {
        // arrange
        val key = booleanPreferencesKey("test_key")
        val value = true
        // edit calls updateData under the hood
        coEvery { dataStore.updateData(any()) } returns mockk()

        // act
        sut.setValue(key, value)

        // assert
        coVerify { dataStore.updateData(any()) }
    }

    @Test
    fun `getValue should return flow with value from datastore`() = runTest {
        // arrange
        val key = booleanPreferencesKey("test_key")
        val value = true
        val preferences = mockk<Preferences> {
            every { this@mockk[key] } returns value
        }
        every { dataStore.data } returns flowOf(preferences)

        // act
        val resultFlow = sut.getValue(key)
        
        // assert
        resultFlow.collect {
            assertEquals(value, it)
        }
    }
}
