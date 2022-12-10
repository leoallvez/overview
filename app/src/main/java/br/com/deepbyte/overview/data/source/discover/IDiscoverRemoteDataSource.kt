package br.com.deepbyte.overview.data.source.discover

import br.com.deepbyte.overview.data.api.response.DiscoverResponse
import br.com.deepbyte.overview.data.source.DataResult

interface IDiscoverRemoteDataSource {
    suspend fun discoverByProviderId(
        providerId: Long,
        page: Int
    ): DataResult<DiscoverResponse>

    suspend fun discoverByGenreId(
        genreId: Long,
        page: Int,
        mediaType: String
    ): DataResult<DiscoverResponse>
}
