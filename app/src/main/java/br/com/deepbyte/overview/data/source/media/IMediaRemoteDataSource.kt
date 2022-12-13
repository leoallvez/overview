package br.com.deepbyte.overview.data.source.media

import br.com.deepbyte.overview.data.api.response.ListResponse
import br.com.deepbyte.overview.data.api.response.MediaDetailResponse
import br.com.deepbyte.overview.data.model.MediaItem
import br.com.deepbyte.overview.data.model.media.Media
import br.com.deepbyte.overview.data.model.media.Movie
import br.com.deepbyte.overview.data.model.media.TvShow
import br.com.deepbyte.overview.data.source.DataResult

interface IMediaRemoteDataSource {
    suspend fun getItem(apiId: Long, type: String): DataResult<MediaDetailResponse>
    suspend fun getMovie(apiId: Long): DataResult<Movie>
    suspend fun getTvShow(apiId: Long): DataResult<TvShow>
    suspend fun getItems(url: String): DataResult<ListResponse<MediaItem>>
    suspend fun search(mediaType: String, query: String): DataResult<ListResponse<MediaItem>>
}
