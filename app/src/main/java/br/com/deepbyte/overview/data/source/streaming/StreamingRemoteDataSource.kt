package br.com.deepbyte.overview.data.source.streaming

import br.com.deepbyte.overview.data.api.ApiService
import br.com.deepbyte.overview.data.api.IApiLocale
import br.com.deepbyte.overview.data.api.response.ProviderResponse
import br.com.deepbyte.overview.data.model.provider.StreamingService
import com.haroldadmin.cnradapter.NetworkResponse
import javax.inject.Inject

class StreamingRemoteDataSource @Inject constructor(
    private val _api: ApiService,
    private val _locale: IApiLocale
) : IStreamingRemoteDataSource {

    override suspend fun getItems(): List<StreamingService> =
        when (val response = getStreamingServices()) {
            is NetworkResponse.Success -> {
                // TODO: create filter logic
                response.body.results.filter { it.apiId > 0 }
            }
            else -> listOf()
        }

    private suspend fun getStreamingServices() = _locale.run {
        _api.getStreamingItems(language = language, region = region)
    }

    override suspend fun getItems(apiId: Long, type: String) =
        when (val response = getProviders(apiId, type)) {
            is NetworkResponse.Success -> mapToStreaming(response, _locale.region)
            else -> listOf()
        }

    private suspend fun getProviders(apiId: Long, type: String) = _locale.run {
        _api.getProviders(id = apiId, type = type, language = language, region = region)
    }

    private fun mapToStreaming(
        response: NetworkResponse.Success<ProviderResponse>,
        region: String
    ): List<StreamingService> {
        val resultsMap = response.body.results
        val entries = resultsMap.filter { it.key == region }.entries
        return if (entries.isNotEmpty()) {
            entries.first().value.getOrderedFlatRate()
        } else {
            listOf()
        }
    }
}
