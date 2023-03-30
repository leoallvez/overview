package br.com.deepbyte.overview.data.repository.media.interfaces

import androidx.paging.PagingData
import br.com.deepbyte.overview.data.source.media.MediaTypeEnum
import br.com.deepbyte.overview.data.model.media.Media
import kotlinx.coroutines.flow.Flow

interface IMediaPagingRepository {
    fun getMediasPaging(
        mediaType: MediaTypeEnum,
        streamingsIds: List<Long>
    ): Flow<PagingData<Media>>
}
