package dev.com.singular.overview.data.source.genre

import dev.com.singular.overview.data.api.response.GenreListResponse
import dev.com.singular.overview.data.source.DataResult
import dev.com.singular.overview.data.source.media.MediaTypeEnum

interface IGenreRemoteDataSource {
    suspend fun getItemByMediaType(type: MediaTypeEnum): DataResult<GenreListResponse>
}
