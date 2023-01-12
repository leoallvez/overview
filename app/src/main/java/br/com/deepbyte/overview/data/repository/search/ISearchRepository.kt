package br.com.deepbyte.overview.data.repository.search

import br.com.deepbyte.overview.data.model.media.Media
import kotlinx.coroutines.flow.Flow

interface ISearchRepository {
    suspend fun search(query: String): Flow<Map<String, List<Media>>>
}
