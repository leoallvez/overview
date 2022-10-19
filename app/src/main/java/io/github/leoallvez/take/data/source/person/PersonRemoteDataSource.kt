package io.github.leoallvez.take.data.source.person

import io.github.leoallvez.take.data.api.ApiService
import io.github.leoallvez.take.data.api.IApiLocale
import io.github.leoallvez.take.data.api.response.PersonResponse
import io.github.leoallvez.take.data.source.DataResult
import io.github.leoallvez.take.data.source.parserResponseToResult
import java.util.*
import javax.inject.Inject

interface IPersonRemoteDataSource {
    suspend fun getPersonDetails(apiId: Long): DataResult<PersonResponse>
}

class PersonRemoteDataSource @Inject constructor(
    private val _api: ApiService,
    _locale: IApiLocale
): IPersonRemoteDataSource {

    private val language = _locale.language()
    private val region = _locale.region

    override suspend fun getPersonDetails(apiId: Long): DataResult<PersonResponse> {
        val response = _api.getPersonDetails(apiId = apiId, language = language, region = region)
        return parserResponseToResult(response)
    }

}
