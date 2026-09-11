package br.dev.singular.overview.data.network.source

import br.dev.singular.overview.data.model.CatalogDataModel
import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.model.WatchProvidersDataModel
import br.dev.singular.overview.data.network.ApiService
import br.dev.singular.overview.data.network.ILocaleProvider
import br.dev.singular.overview.data.network.response.ListResponse
import br.dev.singular.overview.data.network.response.MapResponse
import br.dev.singular.overview.data.remote.config.IRemoteConfigProvider
import br.dev.singular.overview.data.remote.config.RemoteConfigKey
import com.haroldadmin.cnradapter.NetworkResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldHaveSize
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
        every { locale.region } returns "BR"
        coEvery {
            provider.getString(RemoteConfigKey.STREAM_BR)
        } returns "[{\"provider_id\": 1, \"provider_name\": \"Netflix\"}]"

        // act
        val result = sut.getAll()

        // assert
        result shouldHaveSize 1
        result.first().id shouldBeEqualTo 1L
        result.first().name shouldBeEqualTo "Netflix"
        coVerify(exactly = 1) { provider.waitAndActivate() }
    }

    @Test
    fun `getAll should return from API when config is empty`() = runTest {
        // arrange
        val catalogs = listOf(
            CatalogDataModel(id = 1, name = "Netflix", priority = 2),
            CatalogDataModel(id = 2, name = "Prime Video", priority = 1),
        )
        val response = mockk<ListResponse<CatalogDataModel>> {
            every { results } returns catalogs
        }

        every { locale.region } returns "BR"
        coEvery { provider.getString(any()) } returns ""
        coEvery { api.getCatalog(region = "BR") } returns NetworkResponse.Success(
            response,
            mockk(),
            200
        )

        // act
        val result = sut.getAll()

        // assert
        result shouldHaveSize 2
        result[0].id shouldBeEqualTo 2L // Sorted by priority in fetchFromApi
        result[1].id shouldBeEqualTo 1L
    }

    @Test
    fun `getAll should return empty list when both config and API fail`() = runTest {
        // arrange
        every { locale.region } returns "BR"
        coEvery { provider.getString(any()) } returns ""
        coEvery { api.getCatalog(any()) } returns NetworkResponse.NetworkError(java.io.IOException())

        // act
        val result = sut.getAll()

        // assert
        result.shouldBeEmpty()
    }

    @Test
    fun `getAll should fallback to API when fetchFromConfig throws exception`() = runTest {
        // arrange
        every { locale.region } returns "BR"
        coEvery { provider.getString(any()) } throws RuntimeException("Config error")

        val catalogs = listOf(CatalogDataModel(id = 1, name = "Netflix"))
        val response = mockk<ListResponse<CatalogDataModel>> {
            every { results } returns catalogs
        }
        coEvery { api.getCatalog(any()) } returns NetworkResponse.Success(response, mockk(), 200)

        // act
        val result = sut.getAll()

        // assert
        result shouldHaveSize 1
        result.first().id shouldBeEqualTo 1L
        coVerify(exactly = 1) { api.getCatalog("BR") }
    }

    @Test
    fun `getAll should fallback to API when fetchFromConfig has malformed JSON`() = runTest {
        // arrange
        every { locale.region } returns "BR"
        coEvery { provider.getString(any()) } returns "{ invalid json }"

        val catalogs = listOf(CatalogDataModel(id = 1, name = "Netflix"))
        val response = mockk<ListResponse<CatalogDataModel>> {
            every { results } returns catalogs
        }
        coEvery { api.getCatalog(any()) } returns NetworkResponse.Success(response, mockk(), 200)

        // act
        val result = sut.getAll()

        // assert
        result shouldHaveSize 1
        result.first().id shouldBeEqualTo 1L
        coVerify(exactly = 1) { api.getCatalog("BR") }
    }

    @Test
    fun `getAll should return empty list when fetchFromApi throws exception and config is empty`() =
        runTest {
            // arrange
            every { locale.region } returns "BR"
            coEvery { provider.getString(any()) } returns ""
            coEvery { api.getCatalog(any()) } throws RuntimeException("API error")

            // act
            val result = sut.getAll()

            // assert
            result.shouldBeEmpty()
        }

    @Test
    fun `getCatalogsByMedia should return flatrate catalogs for the correct region when API returns success`() =
        runTest {
            // arrange
            val id = 123L
            val type = MediaDataType.MOVIE
            val region = "BR"
            val catalogs = listOf(
                CatalogDataModel(id = 1, name = "Netflix", priority = 2),
                CatalogDataModel(id = 2, name = "Prime Video", priority = 1),
            )
            val watchProviders = WatchProvidersDataModel(flatRate = catalogs)
            val response = MapResponse(results = mapOf(region to watchProviders))

            every { locale.region } returns region
            coEvery {
                api.getWatchProviders(
                    type.key,
                    id
                )
            } returns NetworkResponse.Success(response, mockk(), 200)

            // act
            val result = sut.getCatalogsByMedia(id, type)

            // assert
            result shouldHaveSize 2
            result[0].id shouldBeEqualTo 2L // Sorted by priority in WatchProvidersDataModel
            result[1].id shouldBeEqualTo 1L
            coVerify(exactly = 1) { api.getWatchProviders(type.key, id) }
        }

    @Test
    fun `getCatalogsByMedia should return empty list when region is not present in results`() =
        runTest {
            // arrange
            val id = 123L
            val region = "BR"
            val response = MapResponse<WatchProvidersDataModel>(results = mapOf("US" to mockk()))

            every { locale.region } returns region
            coEvery {
                api.getWatchProviders(
                    any(),
                    any()
                )
            } returns NetworkResponse.Success(response, mockk(), 200)

            // act
            val result = sut.getCatalogsByMedia(id, MediaDataType.MOVIE)

            // assert
            result.shouldBeEmpty()
        }

    @Test
    fun `getCatalogsByMedia should return empty list when API returns failure`() = runTest {
        // arrange
        every { locale.region } returns "BR"
        coEvery { api.getWatchProviders(any(), any()) } returns NetworkResponse.UnknownError(
            Throwable(),
            mockk()
        )

        // act
        val result = sut.getCatalogsByMedia(1L, MediaDataType.MOVIE)

        // assert
        result.shouldBeEmpty()
    }

    @Test
    fun `getCatalogsByMedia should return empty list when API call throws exception`() = runTest {
        // arrange
        every { locale.region } returns "BR"
        coEvery { api.getWatchProviders(any(), any()) } throws RuntimeException("Unexpected error")

        // act
        val result = sut.getCatalogsByMedia(1L, MediaDataType.MOVIE)

        // assert
        result.shouldBeEmpty()
    }
}
