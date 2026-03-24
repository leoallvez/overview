package br.dev.singular.overview.data.network.source

import br.dev.singular.overview.core.remote.RemoteConfigKey
import br.dev.singular.overview.core.remote.IRemoteConfigProvider
import br.dev.singular.overview.data.model.StreamingDataModel
import br.dev.singular.overview.data.network.ApiService
import br.dev.singular.overview.data.network.ILocaleProvider
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

interface IStreamingRemoteDataSource {
    suspend fun getAll(): List<StreamingDataModel>
}

class StreamingRemoteDataSource @Inject constructor(
    private val json: Json,
    private val api: ApiService,
    private val locale: ILocaleProvider,
    private val provider: IRemoteConfigProvider
) : IStreamingRemoteDataSource {

    override suspend fun getAll() = locale.run {
        provider.waitAndActivate()
        fetchFromConfig(region).ifEmpty { fetchFromApi() }
    }

    private suspend fun fetchFromConfig(region: String): List<StreamingDataModel> {
        return try {
            val jsonString = provider.getString(RemoteConfigKey.getStreamingKeyByRegion(region))
            if (jsonString.isBlank()) return emptyList()
            
            withContext(Dispatchers.Default) {
                json.decodeFromString<List<StreamingDataModel>>(jsonString)
            }
        } catch (e: Exception) {
            Timber.e(e)
            emptyList()
        }
    }

    private suspend fun fetchFromApi(): List<StreamingDataModel> {
        return try {
            when (val response = api.getStreaming()) {
                is NetworkResponse.Success -> response.body.results
                else -> listOf()
            }
        } catch (e: Exception) {
            Timber.e(e)
            emptyList()
        }
    }
}
