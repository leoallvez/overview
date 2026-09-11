package br.dev.singular.overview.domain.usecase.media

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.QueryState
import br.dev.singular.overview.domain.repository.GetPage
import br.dev.singular.overview.domain.repository.Page
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.runSafely

interface IGetAllLocalMediasUseCase {
    suspend operator fun invoke(query: QueryState): UseCaseState<Page<Media>>
}

class GetAllLocalMediasUseCase(
    private val getter: GetPage<Media, QueryState>
) : IGetAllLocalMediasUseCase {

    override suspend fun invoke(query: QueryState) = runSafely { getter.getPage(query) }
}
