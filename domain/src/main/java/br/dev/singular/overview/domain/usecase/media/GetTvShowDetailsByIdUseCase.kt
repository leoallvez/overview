package br.dev.singular.overview.domain.usecase.media

import br.dev.singular.overview.domain.model.TvShowDetails
import br.dev.singular.overview.domain.repository.GetById
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.runSafely

interface IGetTvShowDetailsByIdUseCase {
    suspend operator fun invoke(id: Long): UseCaseState<TvShowDetails?>
}

class GetTvShowDetailsByIdUseCase(
    private val getter: GetById<TvShowDetails>
) : IGetTvShowDetailsByIdUseCase {
    override suspend fun invoke(id: Long) = runSafely { getter.getById(id) }
}
