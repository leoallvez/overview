package br.com.deepbyte.overview.data.source.provider

import br.com.deepbyte.overview.data.api.ApiService
import br.com.deepbyte.overview.data.api.IApiLocale
import br.com.deepbyte.overview.data.api.response.ProviderResponse
import br.com.deepbyte.overview.data.source.DataResult
import br.com.deepbyte.overview.data.source.responseToResult
import javax.inject.Inject

interface IProviderRemoteDataSource {
    suspend fun getProvidersResult(id: Long, type: String): DataResult<ProviderResponse>
}

class ProviderRemoteDataSource @Inject constructor(
    private val _api: ApiService,
    private val _locale: IApiLocale
) : IProviderRemoteDataSource {

    override suspend fun getProvidersResult(id: Long, type: String) = responseToResult(
        _api.getProviders(
            id = id, type = type, language = _locale.language, region = _locale.region
        )
    )
}
