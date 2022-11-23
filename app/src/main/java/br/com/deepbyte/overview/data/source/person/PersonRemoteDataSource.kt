package br.com.deepbyte.overview.data.source.person

import br.com.deepbyte.overview.data.api.ApiService
import br.com.deepbyte.overview.data.api.IApiLocale
import br.com.deepbyte.overview.data.api.response.PersonResponse
import br.com.deepbyte.overview.data.source.DataResult
import br.com.deepbyte.overview.data.source.responseToResult
import javax.inject.Inject

interface IPersonRemoteDataSource {
    suspend fun getPersonDetails(apiId: Long): DataResult<PersonResponse>
}

class PersonRemoteDataSource @Inject constructor(
    private val _api: ApiService,
    private val _locale: IApiLocale
) : IPersonRemoteDataSource {

    override suspend fun getPersonDetails(apiId: Long) = responseToResult(
        _api.getPersonDetails(id = apiId, language = _locale.language, region = _locale.region)
    )
}
