package br.dev.singular.overview.data.repository.media.interfaces

import br.dev.singular.overview.data.model.media.Media
import br.dev.singular.overview.data.model.media.MediaEntity
import br.dev.singular.overview.data.source.DataResult
import br.dev.singular.overview.data.source.media.MediaTypeEnum
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface IMediaRepository {
    suspend fun getItem(apiId: Long, type: MediaTypeEnum): Flow<DataResult<out Media>>
    suspend fun update(media: MediaEntity)
    suspend fun deleteUnlikedOlderThan(date: Date)
}
