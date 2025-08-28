package br.dev.singular.overview.data.repository.media.remote.interfaces

import androidx.paging.Pager
import br.dev.singular.overview.data.model.MediaDataModel
import br.dev.singular.overview.data.model.filters.SearchFilters

interface IMediaPagingRepository {
    fun getPaging(filters: SearchFilters): Pager<Int, MediaDataModel>
}
