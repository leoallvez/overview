package br.dev.singular.overview.domain.usecase.media

import br.dev.singular.overview.domain.model.Filter
import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.repository.GetAllByParam
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState

interface IGetMediasUseCase {
    suspend operator fun invoke(filter: Filter): UseCaseState<List<Media>>
}

class GetMediasUseCase(
    private val getter: GetAllByParam<Media, Filter>
) : IGetMediasUseCase {

    override suspend fun invoke(filter: Filter): UseCaseState<List<Media>> {
        return runCatching {
            getter.getAllByParam(param = filter)
        }.fold (
            onSuccess = { medias -> UseCaseState.Success(medias) },
            onFailure = { UseCaseState.Failure(FailType.Exception(it)) }
        )
    }
}
