package br.dev.singular.overview.data.repository.media.local.interfaces

import androidx.paging.Pager
import br.dev.singular.overview.data.model.filters.SearchFilters
import br.dev.singular.overview.data.model.media.MediaEntity

interface IMediaEntityPagingRepository {
    fun getLikedPaging(filters: SearchFilters): Pager<Int, MediaEntity>
}
