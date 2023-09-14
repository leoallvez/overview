package br.com.deepbyte.overview.data.repository.search

import androidx.paging.PagingData
import br.com.deepbyte.overview.data.model.filters.SearchFilters
import br.com.deepbyte.overview.data.model.media.Media
import kotlinx.coroutines.flow.Flow

interface ISearchPagingRepository {
    fun searchPaging(searchFilters: SearchFilters): Flow<PagingData<Media>>
}
