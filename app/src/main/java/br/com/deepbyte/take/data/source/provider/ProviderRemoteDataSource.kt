package br.com.deepbyte.take.data.source.provider

import br.com.deepbyte.take.data.api.ApiService
import br.com.deepbyte.take.data.api.IApiLocale
import br.com.deepbyte.take.data.api.response.ProviderResponse
import br.com.deepbyte.take.data.source.DataResult
import br.com.deepbyte.take.data.source.responseToResult
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
