package br.com.deepbyte.overview.data.source.genre

import br.com.deepbyte.overview.data.MediaType
import br.com.deepbyte.overview.data.api.response.GenreListResponse
import br.com.deepbyte.overview.data.source.DataResult

interface IGenreRemoteDataSource {
    suspend fun getItemByMediaType(mediaType: MediaType): DataResult<GenreListResponse>
}
