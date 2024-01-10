package br.dev.singular.overview.data.repository.media.local.interfaces

import androidx.paging.PagingSource
import br.dev.singular.overview.data.model.media.MediaEntity

interface IMediaEntityPagingRepository {

    suspend fun getLikedPaging(): PagingSource<Int, MediaEntity>

    suspend fun getLikedByTypePaging(type: String): PagingSource<Int, MediaEntity>
}
