package br.com.deepbyte.overview.data.source.discover

import br.com.deepbyte.overview.data.api.response.DiscoverResponse
import br.com.deepbyte.overview.data.source.DataResult

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