package dev.com.singular.overview.data.repository.media.interfaces

import androidx.paging.PagingData
import dev.com.singular.overview.data.model.filters.SearchFilters
import dev.com.singular.overview.data.model.media.Media
import kotlinx.coroutines.flow.Flow

interface IMediaPagingRepository {
    fun getMediasPaging(searchFilters: SearchFilters): Flow<PagingData<Media>>
}
