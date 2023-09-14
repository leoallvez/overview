package br.com.deepbyte.overview.data.repository.media.interfaces

import androidx.paging.PagingData
import br.com.deepbyte.overview.data.model.filters.SearchFilters
import br.com.deepbyte.overview.data.model.media.Media
import kotlinx.coroutines.flow.Flow

interface IMediaPagingRepository {
    fun getMediasPaging(searchFilters: SearchFilters): Flow<PagingData<Media>>
}
