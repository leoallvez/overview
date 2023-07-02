package br.com.deepbyte.overview.data.repository.search

import androidx.paging.PagingData
import br.com.deepbyte.overview.data.model.filters.Filters
import br.com.deepbyte.overview.data.model.media.Media
import kotlinx.coroutines.flow.Flow

interface ISearchPagingRepository {
    fun searchPaging(filters: Filters): Flow<PagingData<Media>>
}
