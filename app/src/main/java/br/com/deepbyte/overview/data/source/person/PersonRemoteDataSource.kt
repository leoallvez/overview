package br.com.deepbyte.overview.data.source.person

import br.com.deepbyte.overview.data.api.ApiService
import br.com.deepbyte.overview.data.api.IApiLocale
import br.com.deepbyte.overview.data.source.responseToResult
import javax.inject.Inject

class PersonRemoteDataSource @Inject constructor(
    private val _api: ApiService,
    private val _locale: IApiLocale
) : IPersonRemoteDataSource {

    override suspend fun getItem(apiId: Long) = _locale.run {
        val response = _api.getPersonItem(id = apiId, language = language, region = region)
        responseToResult(response)
    }

    override suspend fun search(query: String) = _locale.run {
        val response = _api.searchPerson(query, language, region, region)
        responseToResult(response)
    }
}
