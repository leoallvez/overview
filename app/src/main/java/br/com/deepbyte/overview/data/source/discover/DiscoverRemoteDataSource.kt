package br.com.deepbyte.overview.data.source.discover

import br.com.deepbyte.overview.data.api.ApiService
import br.com.deepbyte.overview.data.api.IApiLocale
import br.com.deepbyte.overview.data.source.responseToResult
import javax.inject.Inject

class DiscoverRemoteDataSource @Inject constructor(
    private val _api: ApiService,
    private val _locale: IApiLocale
) : IDiscoverRemoteDataSource {

    override suspend fun discoverByProviderId(providerId: Long, page: Int) =
        responseToResult(discoverByProvider(providerId, page))

    override suspend fun discoverByGenreId(genreId: Long, page: Int, mediaType: String) =
        responseToResult(discoverWithGenre(genreId, mediaType, page))

    private suspend fun discoverByProvider(providerId: Long, page: Int) =
        _locale.run {
            _api.discoverByProvider(providerId, page, language, region, region)
        }

    private suspend fun discoverWithGenre(genreId: Long, mediaType: String, page: Int) =
        _locale.run {
            _api.discoverByGenre(mediaType, genreId, page, language, region, region)
        }
}
