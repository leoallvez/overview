package br.dev.singular.overview.data.repository.media.remote.interfaces

import androidx.paging.PagingData
import br.dev.singular.overview.data.model.filters.SearchFilters
import br.dev.singular.overview.data.model.media.Media
import kotlinx.coroutines.flow.Flow

interface IMediaPagingRepository {
    fun getPaging(searchFilters: SearchFilters): Flow<PagingData<Media>>
}
