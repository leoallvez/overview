package br.com.deepbyte.overview.data.source.media

import br.com.deepbyte.overview.data.api.response.ListContentResponse
import br.com.deepbyte.overview.data.api.response.MediaDetailResponse
import br.com.deepbyte.overview.data.api.response.SearchMediaResponse
import br.com.deepbyte.overview.data.model.MediaItem
import br.com.deepbyte.overview.data.source.DataResult

interface IMediaRemoteDataSource {
    suspend fun getItem(id: Long, type: String): DataResult<MediaDetailResponse>
    suspend fun getItems(url: String): DataResult<ListContentResponse<MediaItem>>
    suspend fun search(mediaType: String, query: String): DataResult<SearchMediaResponse>
}
