package br.com.deepbyte.overview.data.source.provider

import br.com.deepbyte.overview.data.api.ApiService
import br.com.deepbyte.overview.data.api.IApiLocale
import br.com.deepbyte.overview.data.api.response.ProviderResponse
import br.com.deepbyte.overview.data.model.provider.StreamingService
import com.haroldadmin.cnradapter.NetworkResponse.Success
import javax.inject.Inject

class ProviderRemoteDataSource @Inject constructor(
    private val _api: ApiService,
    private val _locale: IApiLocale
) : IProviderRemoteDataSource {

    override suspend fun getItems(apiId: Long, type: String) =
        when (val response = getProviders(apiId, type)) {
            is Success -> filterResponseResults(response, _locale.region)
            else -> listOf()
        }

    private suspend fun getProviders(apiId: Long, type: String) = _locale.run {
        _api.getProviders(id = apiId, type = type, language = language, region = region)
    }

    private fun filterResponseResults(
        response: Success<ProviderResponse>,
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
