package br.com.deepbyte.overview.data.repository.media.interfaces

import br.com.deepbyte.overview.data.model.media.Media
import br.com.deepbyte.overview.data.source.DataResult
import br.com.deepbyte.overview.data.source.media.MediaTypeEnum
import kotlinx.coroutines.flow.Flow

interface IMediaRepository {
    suspend fun getItem(apiId: Long, type: MediaTypeEnum): Flow<DataResult<out Media>>
}
