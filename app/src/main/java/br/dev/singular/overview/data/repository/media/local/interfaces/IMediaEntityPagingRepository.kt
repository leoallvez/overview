package br.dev.singular.overview.data.repository.media.local.interfaces

import androidx.paging.Pager
import br.dev.singular.overview.data.model.media.MediaEntity
import br.dev.singular.overview.data.source.media.MediaType

interface IMediaEntityPagingRepository {
    fun getLikedPaging(type: MediaType): Pager<Int, MediaEntity>
}
