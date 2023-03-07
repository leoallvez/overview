package br.com.deepbyte.overview.data.repository.media.interfaces

import br.com.deepbyte.overview.data.model.media.Media
import br.com.deepbyte.overview.data.source.DataResult
import kotlinx.coroutines.flow.Flow

interface IMediaItemRepository {
    suspend fun getItem(apiId: Long, mediaType: String): Flow<DataResult<out Media>>
}
