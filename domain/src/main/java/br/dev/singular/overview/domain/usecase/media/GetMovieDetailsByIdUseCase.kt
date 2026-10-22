package br.dev.singular.overview.domain.usecase.media

import br.dev.singular.overview.domain.model.MovieDetails
import br.dev.singular.overview.domain.repository.GetById
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.runSafely

interface IGetMovieDetailsByIdUseCase {
    suspend operator fun invoke(id: Long): UseCaseState<MovieDetails?>
}

class GetMovieDetailsByIdUseCase(
    private val getter: GetById<MovieDetails>
) : IGetMovieDetailsByIdUseCase {
    override suspend fun invoke(id: Long) = runSafely { getter.getById(id) }
}
