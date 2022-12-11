package br.com.deepbyte.overview.data.repository.media

import br.com.deepbyte.overview.data.api.response.MediaDetailResponse
import br.com.deepbyte.overview.data.source.DataResult
import kotlinx.coroutines.flow.Flow

interface IMediaRepository {
    suspend fun getItem(apiId: Long, mediaType: String): Flow<DataResult<MediaDetailResponse>>
}
