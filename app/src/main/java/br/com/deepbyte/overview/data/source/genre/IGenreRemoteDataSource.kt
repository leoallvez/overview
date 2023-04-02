package br.com.deepbyte.overview.data.source.genre

import br.com.deepbyte.overview.data.api.response.GenreListResponse
import br.com.deepbyte.overview.data.source.DataResult
import br.com.deepbyte.overview.data.source.media.MediaTypeEnum

interface IGenreRemoteDataSource {
    suspend fun getItemByMediaType(type: MediaTypeEnum): DataResult<GenreListResponse>
}
