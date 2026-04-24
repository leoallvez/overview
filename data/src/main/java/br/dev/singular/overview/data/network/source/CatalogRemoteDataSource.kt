package br.dev.singular.overview.data.network.source

import br.dev.singular.overview.core.remote.IRemoteConfigProvider
import br.dev.singular.overview.core.remote.RemoteConfigKey
import br.dev.singular.overview.data.model.CatalogDataModel
import br.dev.singular.overview.data.network.ApiService
import br.dev.singular.overview.data.network.ILocaleProvider
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

interface ICatalogRemoteDataSource {
    suspend fun getAll(): List<CatalogDataModel>
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
            val jsonString = provider.getString(RemoteConfigKey.getCatalogKeyByRegion(region))
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
}
