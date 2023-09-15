package dev.com.singular.overview.data.repository.media.interfaces

import dev.com.singular.overview.data.source.DataResult
import dev.com.singular.overview.data.source.media.MediaTypeEnum
import dev.com.singular.overview.data.model.media.Media
import kotlinx.coroutines.flow.Flow

interface IMediaRepository {
    suspend fun getItem(apiId: Long, type: MediaTypeEnum): Flow<DataResult<out Media>>
}
