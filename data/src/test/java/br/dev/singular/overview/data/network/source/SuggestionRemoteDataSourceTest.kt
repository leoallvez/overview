package br.dev.singular.overview.data.network.source

import br.dev.singular.overview.core.remote.IRemoteConfigProvider
import br.dev.singular.overview.data.model.SuggestionDataModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SuggestionRemoteDataSourceTest {

    private val provider: IRemoteConfigProvider = mockk()
    private val sut = SuggestionRemoteDataSource(provider)

    @Test
    fun `getAll should return suggestions from config`() = runTest {
        // arrange
        val suggestions = listOf(SuggestionDataModel(id = 1, sourceKey = "trending"))
        
        coEvery { provider.getString(any()) } returns "[{\"id\": 1, \"sourceKey\": \"trending\"}]"

        // act
        val result = sut.getAll()

        // assert
        assertEquals(suggestions.first().id, result.first().id)
        assertEquals(suggestions.first().sourceKey, result.first().sourceKey)
    }
}
