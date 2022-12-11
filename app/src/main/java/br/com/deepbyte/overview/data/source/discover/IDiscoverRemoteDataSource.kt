package br.com.deepbyte.overview.data.source.discover

import br.com.deepbyte.overview.data.api.response.PagingResponse
import br.com.deepbyte.overview.data.model.MediaItem
import br.com.deepbyte.overview.data.source.DataResult

interface IDiscoverRemoteDataSource {
    suspend fun discoverByProviderId(
        providerId: Long,
        page: Int
    ): DataResult<PagingResponse<MediaItem>>

    suspend fun discoverByGenreId(
        genreId: Long,
        page: Int,
        mediaType: String
    ): DataResult<PagingResponse<MediaItem>>
}
