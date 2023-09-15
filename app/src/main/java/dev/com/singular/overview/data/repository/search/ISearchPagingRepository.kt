package dev.com.singular.overview.data.repository.search

import androidx.paging.PagingData
import dev.com.singular.overview.data.model.filters.SearchFilters
import dev.com.singular.overview.data.model.media.Media
import kotlinx.coroutines.flow.Flow

interface ISearchPagingRepository {
    fun searchPaging(searchFilters: SearchFilters): Flow<PagingData<Media>>
}
