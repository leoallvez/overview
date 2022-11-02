package io.github.leoallvez.take.data.source.discover

import io.github.leoallvez.take.data.api.ApiService
import io.github.leoallvez.take.data.api.IApiLocale
import io.github.leoallvez.take.data.api.response.DiscoverResponse
import io.github.leoallvez.take.data.source.DataResult
import io.github.leoallvez.take.data.source.responseToResult
import javax.inject.Inject

interface IDiscoverRemoteDataSource {
    suspend fun discoverOnTvByProvider(
        providerId: Long,
        page: Int
    ): DataResult<DiscoverResponse>
}

class DiscoverRemoteDataSource @Inject constructor(
    private val _api: ApiService,
    private val _locale: IApiLocale
) : IDiscoverRemoteDataSource {

    override suspend fun discoverOnTvByProvider(providerId: Long, page: Int) =
        responseToResult(discoverByProvider(providerId, page))

    private suspend fun discoverByProvider(providerId: Long, page: Int) =
        _api.discoverOnTvByProvider(providerId, page, _locale.language, _locale.region)
}
