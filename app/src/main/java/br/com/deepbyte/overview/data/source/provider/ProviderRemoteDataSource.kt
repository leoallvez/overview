package br.com.deepbyte.overview.data.source.provider

import br.com.deepbyte.overview.data.api.ApiService
import br.com.deepbyte.overview.data.api.IApiLocale
import br.com.deepbyte.overview.data.api.response.ProviderResponse
import br.com.deepbyte.overview.data.model.provider.ProviderPlace
import com.haroldadmin.cnradapter.NetworkResponse.Success
import javax.inject.Inject

class ProviderRemoteDataSource @Inject constructor(
    private val _api: ApiService,
    private val _locale: IApiLocale
) : IProviderRemoteDataSource {

    override suspend fun getItems(apiId: Long, type: String): List<ProviderPlace> = _locale.run {
        val response = _api
            .getProviders(id = apiId, type = type, language = language, region = region)

        when (response) {
            is Success -> filterResponseResults(response, region)
            else -> listOf()
        }
    }

    private fun filterResponseResults(
        response: Success<ProviderResponse>,
        region: String
    ): List<ProviderPlace> {
        val resultsMap = response.body.results
        val entries = resultsMap.filter { it.key == region }.entries
        return if (entries.isNotEmpty()) {
            entries.first().value.getOrderedFlatRate()
        } else {
            listOf()
        }
    }
}
