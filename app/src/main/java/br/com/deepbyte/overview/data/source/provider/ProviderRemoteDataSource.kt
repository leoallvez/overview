package br.com.deepbyte.overview.data.source.provider

import br.com.deepbyte.overview.data.api.ApiService
import br.com.deepbyte.overview.data.api.IApiLocale
import br.com.deepbyte.overview.data.source.responseToResult
import javax.inject.Inject

class ProviderRemoteDataSource @Inject constructor(
    private val _api: ApiService,
    private val _locale: IApiLocale
) : IProviderRemoteDataSource {

    override suspend fun getItems(id: Long, type: String) = responseToResult(
        _locale.run {
            _api.getProviders(id = id, type = type, language = language, region = region)
        }
    )
}
