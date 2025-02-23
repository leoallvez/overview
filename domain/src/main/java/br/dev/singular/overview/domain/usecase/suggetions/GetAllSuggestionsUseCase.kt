package br.dev.singular.overview.domain.usecase.suggetions

import br.dev.singular.overview.domain.model.Suggestion
import br.dev.singular.overview.domain.repository.GetAll
import br.dev.singular.overview.domain.repository.IMediaRepository
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState

interface IGetAllSuggestionsUseCase {
    suspend operator fun invoke(): UseCaseState<List<Suggestion>>
}

class GetAllSuggestionsUseCase(
    private val mediaRepository: IMediaRepository,
    private val suggestionGetter: GetAll<Suggestion>
) : IGetAllSuggestionsUseCase {

    override suspend fun invoke(): UseCaseState<List<Suggestion>> {
        return runCatching {
            suggestionGetter.getAll()
                .filter { it.isActive }
                .map { suggestion ->
                    val medias = mediaRepository.getByPath(suggestion.path).take(MAX_MEDIA)
                    suggestion.copy(medias = medias)
                }
        }.fold(
            onSuccess = { suggestions ->
                suggestions.ifEmpty { return UseCaseState.Failure(FailType.NothingFound) }
                UseCaseState.Success(suggestions.sortedBy { it.order })
            },
            onFailure = { UseCaseState.Failure(FailType.Exception(it)) }
        )
    }

    private companion object {
        const val MAX_MEDIA = 10
    }
}
