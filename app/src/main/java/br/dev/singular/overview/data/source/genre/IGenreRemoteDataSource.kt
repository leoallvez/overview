package br.dev.singular.overview.data.source.genre

import br.dev.singular.overview.data.api.response.GenreListResponse
import br.dev.singular.overview.data.source.DataResult
import br.dev.singular.overview.data.source.media.MediaType

interface IGenreRemoteDataSource {
    suspend fun getItemByMediaType(type: MediaType): DataResult<GenreListResponse>
}
