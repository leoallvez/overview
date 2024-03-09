package br.dev.singular.overview.data.repository.media.remote.interfaces

import androidx.paging.Pager
import br.dev.singular.overview.data.model.filters.SearchFilters
import br.dev.singular.overview.data.model.media.MediaEntity

interface IMediaSearchPagingRepository {
    fun searchPaging(filters: SearchFilters): Pager<Int, MediaEntity>
}
