package br.com.deepbyte.overview.data.source.person

import br.com.deepbyte.overview.data.api.ApiService
import br.com.deepbyte.overview.data.api.IApiLocale
import br.com.deepbyte.overview.data.source.responseToResult
import javax.inject.Inject

class PersonRemoteDataSource @Inject constructor(
    private val _api: ApiService,
    private val _locale: IApiLocale
) : IPersonRemoteDataSource {

    override suspend fun getItem(apiId: Long) = responseToResult(
        _api.getPersonDetails(id = apiId, language = _locale.language, region = _locale.region)
    )

    override suspend fun search(query: String) = _locale.run {
        responseToResult(_api.searchPerson(query, language, region, region))
    }
}
