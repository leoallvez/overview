package br.com.deepbyte.overview.data.source.media

import br.com.deepbyte.overview.data.api.response.ListResponse
import br.com.deepbyte.overview.data.model.MediaItem
import br.com.deepbyte.overview.data.source.DataResult

// TODO: refactor - this will be deleted
interface MediaItemRemoteDataSource {
    suspend fun getItems(url: String): DataResult<ListResponse<MediaItem>>
}
