package br.dev.singular.overview.domain.usecase.media

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.model.QueryState
import br.dev.singular.overview.domain.repository.GetPage
import br.dev.singular.overview.domain.repository.Page
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.runSafely
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

interface IGetRemoteMediasUseCase {
    suspend operator fun invoke(query: QueryState): UseCaseState<Page<Media>>
}

open class BaseGetRemoteMediasUseCase(
    private val keyPrefix: String,
    private val getter: GetPage<Media, QueryState>,
) : IGetRemoteMediasUseCase {

    private val types = listOf(MediaType.MOVIE, MediaType.TV)

    private val MediaType.formatKey: String
        get() = "${keyPrefix}_${name.lowercase()}"

    override suspend operator fun invoke(query: QueryState) = runSafely {
        when (query.type) {
            in types -> fetchPageForType(query, type = query.type)
            MediaType.ALL -> fetchAndCombineTypes(query)
            else -> Page()
        }
    }

    private suspend fun fetchPageForType(query: QueryState, type: MediaType) =
        getter.getPage(param = query.copy(key = type.formatKey))
            .map { it.copy(type = type) }

    private suspend fun fetchAndCombineTypes(query: QueryState) = coroutineScope {
        types
            .map { type -> async { fetchPageForType(query, type) } }
            .awaitAll()
            .reduce { acc, page -> acc + page }
    }
}
