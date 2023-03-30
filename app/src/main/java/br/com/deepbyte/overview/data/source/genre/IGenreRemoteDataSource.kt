package br.com.deepbyte.overview.data.source.genre

import br.com.deepbyte.overview.data.source.media.MediaTypeEnum
import br.com.deepbyte.overview.data.api.response.GenreListResponse
import br.com.deepbyte.overview.data.source.DataResult

interface IGenreRemoteDataSource {
    suspend fun getItemByMediaType(mediaType: MediaTypeEnum): DataResult<GenreListResponse>
}
