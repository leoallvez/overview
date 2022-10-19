package io.github.leoallvez.take.data.source.person

import io.github.leoallvez.take.data.api.ApiService
import io.github.leoallvez.take.data.api.IApiLocale
import io.github.leoallvez.take.data.api.response.PersonResponse
import io.github.leoallvez.take.data.source.DataResult
import io.github.leoallvez.take.data.source.parserResponseToResult
import javax.inject.Inject

interface IPersonRemoteDataSource {
    suspend fun getPersonDetails(apiId: Long): DataResult<PersonResponse>
}

class PersonRemoteDataSource @Inject constructor(
    private val _api: ApiService,
    private val _locale: IApiLocale
): IPersonRemoteDataSource {

    override suspend fun getPersonDetails(apiId: Long) = parserResponseToResult(
        _api.getPersonDetails(id = apiId, language = _locale.language, region = _locale.region)
    )
}
