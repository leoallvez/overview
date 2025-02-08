package br.dev.singular.overview.domain.usecase.suggetions

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.model.Suggestion
import br.dev.singular.overview.domain.repository.GetAll
import br.dev.singular.overview.domain.repository.IMediaRepository
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState

interface IGetAllSuggestionsUseCase {
    suspend operator fun invoke(): UseCaseState<List<Suggestion>>
}

class GetAllSuggestionsUseCase(
    private val getter: GetAll<Suggestion>,
    private val repository: IMediaRepository
) : IGetAllSuggestionsUseCase {

    override suspend fun invoke(): UseCaseState<List<Suggestion>> {
        return runCatching {
            getter.getAll()
                .filter { it.isActive }
                .mapNotNull { suggestion ->
                    val medias = getMediasByPath(suggestion.path, suggestion.type)
                    if (medias.isNotEmpty()) suggestion.copy(medias = medias) else null
                }
        }.fold(
            onSuccess = { suggestions ->
                suggestions.ifEmpty { return UseCaseState.Failure(FailType.NothingFound) }
                UseCaseState.Success(suggestions.sortedBy { it.order })
            },
            onFailure = { UseCaseState.Failure(FailType.Exception(it)) }
        )
    }

    private suspend fun getMediasByPath(path: String, type: MediaType): List<Media> {
        val result = repository.getByPath(path).take(MAX_MEDIA)
        return when (type) {
            MediaType.ALL -> result
            else -> result.map { it.copy(type = type) }
        }
    }

    companion object {
        const val MAX_MEDIA = 10
    }
}
