package br.dev.singular.overview.data.network.source

import br.dev.singular.overview.data.model.CatalogDataModel
import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.model.WatchProvidersDataModel
import br.dev.singular.overview.data.network.ApiService
import br.dev.singular.overview.data.network.ILocaleProvider
import br.dev.singular.overview.data.remote.config.IRemoteConfigProvider
import br.dev.singular.overview.data.remote.config.RemoteConfigKey
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

interface ICatalogRemoteDataSource {
    suspend fun getAll(): List<CatalogDataModel>
    suspend fun getCatalogsByMedia(id: Long, type: MediaDataType): List<CatalogDataModel>
}

class CatalogRemoteDataSource @Inject constructor(
    private val json: Json,
    private val api: ApiService,
    private val locale: ILocaleProvider,
    private val provider: IRemoteConfigProvider
) : ICatalogRemoteDataSource {

    override suspend fun getAll() = locale.run {
        provider.waitAndActivate()
        fetchFromConfig(region).ifEmpty { fetchFromApi(region) }
    }

    private suspend fun fetchFromConfig(region: String): List<CatalogDataModel> {
        return try {
            val jsonString = provider.getString(RemoteConfigKey.getKeyByRegion(region))
            if (jsonString.isBlank()) return emptyList()

            withContext(Dispatchers.Default) {
                json.decodeFromString<List<CatalogDataModel>>(jsonString)
            }
        } catch (e: Exception) {
            Timber.e(e)
            emptyList()
        }
    }

    private suspend fun fetchFromApi(region: String): List<CatalogDataModel> {
        return try {
            when (val response = api.getCatalog(region = region)) {
                is NetworkResponse.Success -> response.body.results.sortedBy { it.priority }
                else -> listOf()
            }
        } catch (e: Exception) {
            Timber.e(e)
            emptyList()
        }
    }

    override suspend fun getCatalogsByMedia(id: Long, type: MediaDataType): List<CatalogDataModel> {
        val region = locale.region
        return try {
            when (val response = api.getWatchProviders(type.key, id)) {
                is NetworkResponse.Success -> mapToCatalogs(response.body.results, region)
                else -> {
                    Timber.w("Failed to fetch watch providers for ${type.key} (ID: $id) in region $region")
                    emptyList()
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Exception fetching watch providers for ${type.key} (ID: $id)")
            emptyList()
        }
    }

    private fun mapToCatalogs(
        resultsMap: Map<String, WatchProvidersDataModel>,
        region: String
    ): List<CatalogDataModel> = resultsMap[region]?.flatRatesByPriority ?: emptyList()
}
