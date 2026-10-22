package br.dev.singular.overview.domain.usecase.genre

import br.dev.singular.overview.domain.model.Genre
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.repository.GetByParam

interface IFetchGenresByTypeUseCase {
    suspend operator fun invoke(type: MediaType): List<Genre>
}

class FetchGenresByTypeUseCase(
    private val getter: GetByParam<List<Genre>, MediaType>
) : IFetchGenresByTypeUseCase {

    override suspend fun invoke(type: MediaType) = runCatching {
        getter.getByParam(param = type).sortedBy { it.name }
    }.fold(
        onSuccess = { it },
        onFailure = { listOf() }
    )
}
