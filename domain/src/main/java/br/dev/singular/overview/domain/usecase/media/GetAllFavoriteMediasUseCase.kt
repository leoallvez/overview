package br.dev.singular.overview.domain.usecase.media

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.repository.GetAll
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState

interface IGetAllFavoriteMediasUseCase {
    suspend operator fun invoke(type: MediaType): UseCaseState<List<Media>>
}

class GetAllFavoriteMediasUseCase(
    private val getter: GetAll<Media>
) : IGetAllFavoriteMediasUseCase {

    override suspend fun invoke(type: MediaType): UseCaseState<List<Media>> {
        return runCatching {
            getter.getAll().filter { it.isLiked && it.type == type }
        }.fold (
            onSuccess = { medias -> UseCaseState.Success(medias) },
            onFailure = { UseCaseState.Failure(FailType.Exception(it)) }
        )
    }
}
