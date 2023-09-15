package dev.com.singular.overview.data.source.streaming

import com.haroldadmin.cnradapter.NetworkResponse
import dev.com.singular.overview.data.api.ApiService
import dev.com.singular.overview.data.api.IApiLocale
import dev.com.singular.overview.data.api.response.ProviderResponse
import dev.com.singular.overview.data.model.provider.StreamingEntity
import javax.inject.Inject

class StreamingRemoteDataSource @Inject constructor(
    private val _api: ApiService,
    private val _locale: IApiLocale
) : IStreamingRemoteDataSource {

    override suspend fun getItems(): List<StreamingEntity> =
        when (val response = getStreamings()) {
            is NetworkResponse.Success -> {
                response.body.results
            }
            else -> listOf()
        }

    private suspend fun getStreamings() = _locale.run {
        _api.getStreamingItems(language = language, region = region)
    }

    override suspend fun getItems(apiId: Long, type: String) =
        when (val response = getProviders(apiId, type)) {
            is NetworkResponse.Success -> mapToStreaming(response, _locale.region)
            else -> listOf()
        }

    private suspend fun getProviders(apiId: Long, type: String) = _locale.run {
        _api.getProviders(id = apiId, mediaType = type, language = language, region = region)
    }

    private fun mapToStreaming(
        response: NetworkResponse.Success<ProviderResponse>,
        region: String
    ): List<StreamingEntity> {
        val resultsMap = response.body.results
        val entries = resultsMap.filter { it.key == region }.entries
        return if (entries.isNotEmpty()) {
            entries.first().value.getOrderedFlatRate()
        } else {
            listOf()
        }
    }
}
