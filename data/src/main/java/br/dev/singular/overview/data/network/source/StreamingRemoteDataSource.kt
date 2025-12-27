package br.dev.singular.overview.data.network.source

import br.dev.singular.overview.core.remote.RemoteConfigKey
import br.dev.singular.overview.core.remote.RemoteConfigProvider
import br.dev.singular.overview.data.model.StreamingDataModel
import br.dev.singular.overview.data.network.ApiService
import br.dev.singular.overview.data.network.LocaleProvider
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

interface IStreamingRemoteDataSource {
    suspend fun getAll(): List<StreamingDataModel>
}

class StreamingRemoteDataSource @Inject constructor(
    private val json: Json,
    private val api: ApiService,
    private val locale: LocaleProvider,
    private val provider: RemoteConfigProvider
) : IStreamingRemoteDataSource {

    override suspend fun getAll() = locale.run {
        fetchFromConfig(region).ifEmpty { fetchFromApi(language, region) }
    }

    fun fetchFromConfig(region: String): List<StreamingDataModel> {
        return try {
            val jsonString = provider.getString(RemoteConfigKey.getStreamingKeyByRegion(region))
            json.decodeFromString<List<StreamingDataModel>>(jsonString)
        } catch (e: Exception) {
            Timber.e(e)
            emptyList()
        }
    }

    suspend fun fetchFromApi(language: String, region: String): List<StreamingDataModel> {
        return try {
            val response = api.getStreaming(language, region)
            when (response) {
                is NetworkResponse.Success -> response.body.results
                else -> listOf()
            }
        } catch (e: Exception) {
            Timber.e(e)
            emptyList()
        }
    }
}
