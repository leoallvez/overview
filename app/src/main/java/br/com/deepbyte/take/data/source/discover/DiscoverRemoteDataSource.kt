package br.com.deepbyte.take.data.source.discover

import br.com.deepbyte.take.data.api.ApiService
import br.com.deepbyte.take.data.api.IApiLocale
import br.com.deepbyte.take.data.api.response.DiscoverResponse
import br.com.deepbyte.take.data.source.DataResult
import br.com.deepbyte.take.data.source.responseToResult
import javax.inject.Inject

interface IDiscoverRemoteDataSource {
    suspend fun discoverOnTvByProvider(
        providerId: Long,
        page: Int
    ): DataResult<DiscoverResponse>

    suspend fun discoverByGenre(
        genreId: Long,
        page: Int,
        mediaType: String
    ): DataResult<DiscoverResponse>
}

class DiscoverRemoteDataSource @Inject constructor(
    private val _api: ApiService,
    private val _locale: IApiLocale
) : IDiscoverRemoteDataSource {

    override suspend fun discoverOnTvByProvider(providerId: Long, page: Int) =
        responseToResult(discoverByProvider(providerId, page))

    override suspend fun discoverByGenre(genreId: Long, page: Int, mediaType: String) =
        responseToResult(discoverWithGenre(genreId, mediaType, page))

    private suspend fun discoverByProvider(providerId: Long, page: Int) =
        _locale.run {
            _api.discoverOnTvByProvider(providerId, page, language, region, region)
        }

    private suspend fun discoverWithGenre(genreId: Long, mediaType: String, page: Int) =
        _locale.run {
            _api.discoverByGenre(mediaType, genreId, page, language, region, region)
        }
}
