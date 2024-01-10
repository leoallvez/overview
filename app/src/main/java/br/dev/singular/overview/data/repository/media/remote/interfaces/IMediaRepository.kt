package br.dev.singular.overview.data.repository.media.remote.interfaces

import br.dev.singular.overview.data.model.media.Media
import br.dev.singular.overview.data.source.DataResult
import br.dev.singular.overview.data.source.media.MediaTypeEnum
import kotlinx.coroutines.flow.Flow

interface IMediaRepository {
    suspend fun getItem(apiId: Long, type: MediaTypeEnum): Flow<DataResult<out Media>>
}
