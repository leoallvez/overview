package br.com.deepbyte.overview.data.repository.search

import kotlinx.coroutines.flow.Flow

interface ISearchRepository {
    suspend fun search(query: String): Flow<SearchContents>
}
