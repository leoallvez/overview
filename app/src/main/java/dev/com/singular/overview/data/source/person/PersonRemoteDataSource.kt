package dev.com.singular.overview.data.source.person

import dev.com.singular.overview.data.api.ApiService
import dev.com.singular.overview.data.api.IApiLocale
import dev.com.singular.overview.data.source.responseToResult
import javax.inject.Inject

class PersonRemoteDataSource @Inject constructor(
    private val _api: ApiService,
    private val _locale: IApiLocale
) : IPersonRemoteDataSource {

    override suspend fun getItem(apiId: Long) = _locale.run {
        val response = _api.getPersonItem(id = apiId, language = language, region = region)
        responseToResult(response)
    }
}
