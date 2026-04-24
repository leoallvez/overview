package br.dev.singular.overview.data.network.source

import br.dev.singular.overview.core.remote.IRemoteConfigProvider
import br.dev.singular.overview.data.model.CatalogDataModel
import br.dev.singular.overview.data.network.ApiService
import br.dev.singular.overview.data.network.ILocaleProvider
import br.dev.singular.overview.data.network.response.ListResponse
import com.haroldadmin.cnradapter.NetworkResponse
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class CatalogRemoteDataSourceTest {

    private val json = Json { ignoreUnknownKeys = true }
    private val api: ApiService = mockk()
    private val locale: ILocaleProvider = mockk()
    private val provider: IRemoteConfigProvider = mockk(relaxed = true)
    private val sut = CatalogRemoteDataSource(json, api, locale, provider)

    @Test
    fun `getAll should return from config when config is available`() = runTest {
        // arrange
        val date = java.util.Date(1000)
        val catalogs = listOf(CatalogDataModel(id = 1, name = "Netflix", lastUpdate = date))
        // Since lastUpdate is @Transient, we need to ensure the decoded ones match or we don't compare the date
        
        every { locale.region } returns "BR"
        coEvery { provider.getString(any()) } returns "[{\"provider_id\": 1, \"provider_name\": \"Netflix\"}]"

        // act
        val result = sut.getAll()

        // assert
        assertEquals(catalogs.first().id, result.first().id)
        assertEquals(catalogs.first().name, result.first().name)
    }

    @Test
    fun `getAll should return from API when config is empty`() = runTest {
        // arrange
        val catalogs = listOf(CatalogDataModel(id = 1, name = "Netflix"))
        val response = mockk<ListResponse<CatalogDataModel>> {
            every { results } returns catalogs
        }
        
        every { locale.region } returns "BR"
        coEvery { provider.getString(any()) } returns ""
        coEvery { api.getCatalog(any()) } returns NetworkResponse.Success(response, mockk(), 200)

        // act
        val result = sut.getAll()

        // assert
        assertEquals(catalogs, result)
    }
}
