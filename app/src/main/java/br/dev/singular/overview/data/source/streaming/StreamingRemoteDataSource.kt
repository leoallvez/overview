package br.dev.singular.overview.data.source.streaming

import br.dev.singular.overview.data.api.ApiService
import br.dev.singular.overview.data.api.IApiLocale
import br.dev.singular.overview.data.api.response.ErrorResponse
import br.dev.singular.overview.data.api.response.ProviderResponse
import br.dev.singular.overview.data.model.provider.StreamingEntity
import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.invoke
import javax.inject.Inject

class StreamingRemoteDataSource @Inject constructor(
    private val _api: ApiService,
    private val _locale: IApiLocale
) : IStreamingRemoteDataSource {

    override suspend fun getItems(): List<StreamingEntity> =
        when (val response = getStreaming()) {
            is NetworkResponse.Success -> response.body.results
            else -> emptyList()
        }

    private suspend fun getStreaming() = _locale.run {
        _api.getStreamingItems(language = language, region = region)
    }

    override suspend fun getItems(apiId: Long, type: String) =
        when (val response = getProviders(apiId, type)) {
            is NetworkResponse.Success -> mapToStreaming(response, _locale.region)
            else -> emptyList()
        }

    private suspend fun getProviders(apiId: Long, type: String) = _locale.run {
        _api.getProviders(id = apiId, mediaType = type, language = language, region = region)
    }

    private fun mapToStreaming(
        response: NetworkResponse<ProviderResponse, ErrorResponse>,
        region: String
    ): List<StreamingEntity> {

        val results = response.invoke()?.results ?: mapOf()
        val entries = results.filter { it.key == region }.entries
        return if (entries.isNotEmpty()) {
            entries.first().value.getOrderedFlatRate() }
        else {
            emptyList()
        }
    }
}
