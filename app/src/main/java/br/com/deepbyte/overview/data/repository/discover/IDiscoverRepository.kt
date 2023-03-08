package br.com.deepbyte.overview.data.repository.discover

import androidx.paging.PagingData
import br.com.deepbyte.overview.data.model.MediaItem
import kotlinx.coroutines.flow.Flow

interface IDiscoverRepository {
    fun discoverByGenreId(genreId: Long, mediaType: String): Flow<PagingData<MediaItem>>
}
