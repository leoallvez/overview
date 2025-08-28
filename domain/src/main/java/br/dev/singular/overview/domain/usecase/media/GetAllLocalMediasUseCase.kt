package br.dev.singular.overview.domain.usecase.media

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.MediaParam
import br.dev.singular.overview.domain.repository.Page
import br.dev.singular.overview.domain.repository.GetPage
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState

interface IGetAllLocalMediasUseCase {
    suspend operator fun invoke(param: MediaParam): UseCaseState<Page<Media>>
}

class GetAllLocalMediasUseCase(
    private val getter: GetPage<Media, MediaParam>
) : IGetAllLocalMediasUseCase {

    override suspend fun invoke(param: MediaParam): UseCaseState<Page<Media>> {
        return runCatching {
            getter.getPage(param)
        }.fold (
            onSuccess = { medias -> UseCaseState.Success(medias) },
            onFailure = { UseCaseState.Failure(FailType.Exception(it)) }
        )
    }
}
